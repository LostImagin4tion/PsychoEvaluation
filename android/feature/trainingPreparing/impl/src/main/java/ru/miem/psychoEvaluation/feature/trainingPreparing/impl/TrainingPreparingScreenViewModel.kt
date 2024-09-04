package ru.miem.psychoEvaluation.feature.trainingPreparing.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.CurrentScreen
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TrainingPreparingScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _currentScreen = MutableStateFlow(CurrentScreen.Welcome)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    val currentScreen: StateFlow<CurrentScreen> = _currentScreen

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
            .scan(initial = 0) { numberOfTicks, _ ->
                numberOfTicks + 1
            }
            .onEach { numberOfTicks ->
                check(numberOfTicks / NUMBER_OF_SCREENS < NUMBER_OF_ROUNDS)
            }
            .catch {
                _currentScreen.emit(CurrentScreen.Exhale)
                onTimerEnded()
            }
            .zip(_currentScreen) { _, screen ->
                when (screen) {
                    CurrentScreen.Welcome -> CurrentScreen.TakeABreath
                    CurrentScreen.TakeABreath -> CurrentScreen.Exhale
                    CurrentScreen.Exhale -> CurrentScreen.TakeABreath
                }
            }
            .onEach { newScreen ->
                _currentScreen.emit(newScreen)
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

    private companion object {
        val TAG: String = TrainingPreparingScreenViewModel::class.java.simpleName

        val DEFAULT_PERIOD = 3.seconds
        val NUMBER_OF_SCREENS = CurrentScreen.entries.size - 1

        const val NUMBER_OF_ROUNDS = 3
    }
}
