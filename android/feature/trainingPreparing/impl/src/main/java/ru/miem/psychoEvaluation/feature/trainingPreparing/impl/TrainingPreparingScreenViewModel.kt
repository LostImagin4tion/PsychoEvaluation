package ru.miem.psychoEvaluation.feature.trainingPreparing.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.TrainingPreparingScreenState
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TrainingPreparingScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _screenState = MutableStateFlow(defaultState)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    val screenState: StateFlow<TrainingPreparingScreenState> = _screenState

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
            .launchIn(viewModelScope)
    }

    fun startCollectingAndNormalizingSensorData(
        usbManager: UsbManager,
        onTimerEnded: () -> Unit,
    ) {
        viewModelScope.launch {
            val deviceType = settingsInteractor.getCurrentSensorDeviceType()
                .first()

            when (deviceType) {
                SensorDeviceType.Usb -> findDataBordersWithUsbDevice(usbManager)
                SensorDeviceType.Bluetooth -> findDataBordersWithBleDevice()
                SensorDeviceType.Unknown -> {
                    Timber.tag(TAG).e("Got unexpected device type $deviceType")
                }
            }
        }

        tickerFlow(period = DEFAULT_PERIOD)
            .cancellable()
            .scan(initial = 0.seconds) { accumulated, _ ->
                accumulated + DEFAULT_PERIOD
            }
            .onEach { numberOfTicks ->
                check(numberOfTicks / ROUND_DURATION < NUMBER_OF_ROUNDS)
            }
            .catch {
                val newScreenState = _screenState.value.copy(currentScreen = CurrentScreen.Exhale)
                _screenState.emit(newScreenState)
                onTimerEnded()
            }
            .combine(_screenState) { numberOfTicks, state ->
                val currentRoundTime = numberOfTicks % ROUND_DURATION

                val timeSpentAtCurrentScreen = when (state.currentScreen) {
                    CurrentScreen.Welcome -> 0.seconds
                    CurrentScreen.TakeABreath -> currentRoundTime
                    CurrentScreen.HoldYourBreath ->
                        currentRoundTime
                            .minus(CurrentScreen.TakeABreath.durationTime)
                    CurrentScreen.Exhale ->
                        currentRoundTime
                            .minus(CurrentScreen.TakeABreath.durationTime)
                            .minus(CurrentScreen.HoldYourBreath.durationTime)
                }

                val newScreen = when (state.currentScreen) {
                    CurrentScreen.Welcome -> CurrentScreen.TakeABreath
                    CurrentScreen.TakeABreath -> {
                        if (timeSpentAtCurrentScreen >= CurrentScreen.TakeABreath.durationTime) {
                            CurrentScreen.HoldYourBreath
                        } else {
                            CurrentScreen.TakeABreath
                        }
                    }
                    CurrentScreen.HoldYourBreath -> {
                        if (timeSpentAtCurrentScreen >= CurrentScreen.HoldYourBreath.durationTime) {
                            CurrentScreen.Exhale
                        } else {
                            CurrentScreen.HoldYourBreath
                        }
                    }
                    CurrentScreen.Exhale -> {
                        if (currentRoundTime == 0.seconds) {
                            CurrentScreen.TakeABreath
                        } else {
                            CurrentScreen.Exhale
                        }
                    }
                }

                val newProgress = 1.0 - timeSpentAtCurrentScreen / newScreen.durationTime
                val roundNumber = (numberOfTicks / ROUND_DURATION).toInt() + 1
                val newState = _screenState.value.copy(
                    currentScreen = newScreen,
                    roundNumberString = ROUND_NUMBER_PLACEHOLDER.format(roundNumber),
                    screenProgress = newProgress
                )
                _screenState.emit(newState)
            }
            .launchIn(viewModelScope)
    }

    fun disconnect() {
        when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.disconnect()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.disconnect()
            SensorDeviceType.Unknown -> {}
        }
    }

    private fun findDataBordersWithUsbDevice(usbManager: UsbManager) {
        viewModelScope.launch {
            usbDeviceInteractor.findDataBorders(usbManager)
        }
    }

    private fun findDataBordersWithBleDevice() {
        viewModelScope.launch {
            bleDeviceInteractor.findDataBorders()
        }
    }

    private fun tickerFlow(period: Duration) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private operator fun Duration.rem(other: Duration): Duration {
        return (this.inWholeMilliseconds % other.inWholeMilliseconds).milliseconds
    }

    private companion object {
        val TAG: String = TrainingPreparingScreenViewModel::class.java.simpleName

        const val NUMBER_OF_ROUNDS = 7
        const val ROUND_NUMBER_PLACEHOLDER = "%s/${NUMBER_OF_ROUNDS}"

        val DEFAULT_PERIOD = 10.milliseconds
        val ROUND_DURATION = CurrentScreen.entries
            .map { it.durationTime }
            .reduce { acc: Duration, duration: Duration ->
                acc + duration
            }

        val defaultState = TrainingPreparingScreenState(
            currentScreen = CurrentScreen.Welcome,
            roundNumberString = ROUND_NUMBER_PLACEHOLDER.format("1"),
            screenProgress = 0.0,
        )
    }
}
