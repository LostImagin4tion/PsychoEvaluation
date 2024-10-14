package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameInProgressState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameSettingsState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameStatisticsState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.toSensorData
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Suppress("MagicNumber")
class AirplaneGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val gameScreenStateMutex = Mutex()
    private val _stressData = MutableStateFlow(defaultSensorData)
    private val _gameScreenState = MutableStateFlow<AirplaneGameState>(stubGameInProgressState)

    val stressData: StateFlow<SensorData> = _stressData
    val gameScreenState: StateFlow<AirplaneGameState> = _gameScreenState

    private val allStress = mutableListOf<Int>()
    val chartModelProducer = CartesianChartModelProducer.build()

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach {
                gameScreenStateMutex.withLock {
                    _gameScreenState.value.let { state ->
                        val newState = when (state) {
                            is AirplaneGameSettingsState -> state.copy(
                                sensorDeviceType = it,
                            )
                            is AirplaneGameInProgressState -> state.copy(
                                sensorDeviceType = it,
                            )
                            is AirplaneGameStatisticsState -> state.copy(
                                sensorDeviceType = it,
                            )
                        }
                        _gameScreenState.emit(newState)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun connectToUsbDevice(usbManager: UsbManager) {
        viewModelScope.launch {
            usbDeviceInteractor.getDeviceData(usbManager) {
                emitNewData(it.toSensorData())
            }
        }
    }

    fun retrieveDataFromBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        bleDeviceHardwareAddress: String,
    ) {
        bleDeviceInteractor.connectToBluetoothDevice(
            activity = activity,
            bluetoothAdapter = bluetoothAdapter,
            deviceHardwareAddress = bleDeviceHardwareAddress,
        )

        viewModelScope.launch {
            bleDeviceInteractor.getDeviceData {
                emitNewData(it.toSensorData())
            }
        }
    }

    fun disconnect() {
        when (_gameScreenState.value.sensorDeviceType) {
            SensorDeviceType.Usb -> usbDeviceInteractor.disconnect()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.disconnect()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun increaseGameDifficulty() {
        when (_gameScreenState.value.sensorDeviceType) {
            SensorDeviceType.Usb -> usbDeviceInteractor.increaseGameDifficulty()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.increaseGameDifficulty()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun decreaseGameDifficulty() {
        when (_gameScreenState.value.sensorDeviceType) {
            SensorDeviceType.Usb -> usbDeviceInteractor.decreaseGameDifficulty()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.decreaseGameDifficulty()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun changeGameParameters(
        gsrUpperBound: Double?,
        gsrLowerBound: Double?,
        gameTime: Duration?,
    ) {
        when (_gameScreenState.value.sensorDeviceType) {
            SensorDeviceType.Usb -> usbDeviceInteractor.changeDataBorders(gsrUpperBound, gsrLowerBound)
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.changeDataBorders(gsrUpperBound, gsrLowerBound)
            SensorDeviceType.Unknown -> {}
        }
        viewModelScope.launch {
            gameScreenStateMutex.withLock {
                val newState = AirplaneGameInProgressState(
                    sensorDeviceType = _gameScreenState.value.sensorDeviceType,
                    maxGameTime = gameTime ?: defaultMaxGameTime
                )
                _gameScreenState.emit(newState)
            }
        }
    }

    fun changeScreen(newScreen: CurrentScreen) {
        viewModelScope.launch {
            gameScreenStateMutex.withLock {
                when (newScreen) {
                    CurrentScreen.AirplaneGameSettings -> {
                        val newScreenState = AirplaneGameSettingsState(
                            sensorDeviceType = _gameScreenState.value.sensorDeviceType,
                            maxGameTime = _gameScreenState.value.maxGameTime,
                        )
                        _gameScreenState.emit(newScreenState)
                    }
                    CurrentScreen.AirplaneGameInProgress -> {
                        val newScreenState = AirplaneGameInProgressState(
                            sensorDeviceType = _gameScreenState.value.sensorDeviceType,
                            maxGameTime = defaultMaxGameTime
                        )
                        _gameScreenState.emit(newScreenState)
                    }
                    CurrentScreen.AirplaneGameStatistics -> {}
                }
            }
        }
    }

    fun goToStatisticsScreen(
        gameTime: Duration,
        timeInCorridor: Duration,
        timeUpperCorridor: Duration,
        timeLowerCorridor: Duration,
        numberOfFlightsOutsideCorridor: Int
    ) {
        viewModelScope.launch {
            val gameStatisticsState = (_gameScreenState.value as AirplaneGameInProgressState)
                .toGameStatisticsState(
                    gameTime = gameTime,
                    timeInCorridor = timeInCorridor,
                    timeUpperCorridor = timeUpperCorridor,
                    timeLowerCorridor = timeLowerCorridor,
                    numberOfFlightsOutsideCorridor = numberOfFlightsOutsideCorridor,
                )
            _gameScreenState.emit(gameStatisticsState)
        }
    }

    fun restartGame() {
        viewModelScope.launch {
            _gameScreenState.run {
                emit(
                    AirplaneGameInProgressState(
                        sensorDeviceType = value.sensorDeviceType,
                        maxGameTime = value.maxGameTime ?: defaultMaxGameTime
                    )
                )
            }
        }
    }

    fun finishGame(
        gameTime: Duration,
        timeInCorridor: Duration,
        timeUpperCorridor: Duration,
        timeLowerCorridor: Duration,
        numberOfFlightsOutsideCorridor: Int
    ) {
    }

    private fun AirplaneGameInProgressState.toGameStatisticsState(
        gameTime: Duration,
        timeInCorridor: Duration,
        timeUpperCorridor: Duration,
        timeLowerCorridor: Duration,
        numberOfFlightsOutsideCorridor: Int
    ): AirplaneGameStatisticsState {
        val gameTimeMillis = gameTime.inWholeMilliseconds
        val timeInCorridorMillis = timeInCorridor.inWholeMilliseconds
        val successPercent = timeInCorridorMillis.toFloat() / gameTimeMillis.toFloat()

        return AirplaneGameStatisticsState(
            sensorDeviceType = sensorDeviceType,
            maxGameTime = maxGameTime,
            successPercent = successPercent,
            gameTime = createTimeString(gameTime),
            timeInCorridor = createTimeString(timeInCorridor),
            timeUpperCorridor = createTimeString(timeUpperCorridor),
            timeLowerCorridor = createTimeString(timeLowerCorridor),
            numberOfFlightsOutsideCorridor = numberOfFlightsOutsideCorridor,
        )
    }

    private suspend fun emitNewData(data: SensorData) {
        _stressData.emit(data)
        allStress.add(data.rawData)
        chartModelProducer.runTransaction {
            lineSeries { series(allStress) }
        }
    }

    private fun createTimeString(duration: Duration): String {
        val minutes = duration.inWholeMinutes.toInt()
        val seconds = duration.inWholeSeconds.toInt() - minutes * 60

        return when {
            minutes >= 10 && seconds >= 10 -> "$minutes:$seconds"
            minutes < 10 && seconds >= 10 -> "0$minutes:$seconds"
            minutes >= 10 && seconds < 10 -> "$minutes:0$seconds"
            else -> "0$minutes:0$seconds"
        }
    }

    private companion object {
        val TAG: String = AirplaneGameScreenViewModel::class.java.simpleName

        val defaultSensorData = SensorData(0, 0.0, 0.0, 0.0)
        val defaultMaxGameTime = 3.minutes
        val defaultGameSettingsState = AirplaneGameSettingsState(
            sensorDeviceType = SensorDeviceType.Unknown,
            maxGameTime = defaultMaxGameTime,
        )
        val stubGameInProgressState = AirplaneGameInProgressState(
            sensorDeviceType = SensorDeviceType.Unknown,
            maxGameTime = 30.seconds
        )
    }
}
