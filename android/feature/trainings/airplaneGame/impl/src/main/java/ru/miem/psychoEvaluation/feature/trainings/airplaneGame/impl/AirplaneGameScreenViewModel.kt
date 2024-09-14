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
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameScreenState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.toSensorData
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AirplaneGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val gameScreenStateMutex = Mutex()
    private val _stressData = MutableStateFlow(defaultSensorData)
    private val _gameScreenState = MutableStateFlow(defaultGameScreenState)

    val stressData: StateFlow<SensorData> = _stressData
    val gameScreenState: StateFlow<AirplaneGameScreenState> = _gameScreenState

    private val allStress = mutableListOf<Int>()
    val chartModelProducer = CartesianChartModelProducer.build()

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach {
                gameScreenStateMutex.withLock {
                    val newState = _gameScreenState.value.copy(
                        sensorDeviceType = it
                    )
                    Timber.tag("HELLO").d("HELLO EMIT settings changes new state $newState")
                    _gameScreenState.emit(newState)
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
                val newState = _gameScreenState.value.run {
                    copy(
                        maxGameTime = gameTime ?: this.maxGameTime,
                        currentScreen = CurrentScreen.AirplaneGame,
                    )
                }
                Timber.tag("HELLO").d("HELLO EMIT game parameters new state $newState")
                _gameScreenState.emit(newState)
            }
        }
    }

    private suspend fun emitNewData(data: SensorData) {
        _stressData.emit(data)
        allStress.add(data.rawData)
        chartModelProducer.runTransaction {
            lineSeries { series(allStress) }
        }
    }

    private companion object {
        val TAG: String = AirplaneGameScreenViewModel::class.java.simpleName

        val defaultSensorData = SensorData(0, 0.0, 0.0, 0.0)
        val defaultGameScreenState = AirplaneGameScreenState(
            sensorDeviceType = SensorDeviceType.Unknown,
            currentScreen = CurrentScreen.AirplaneGameSettings,
            maxGameTime = 3.minutes
        )
    }
}
