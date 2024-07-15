package ru.miem.psychoEvaluation.feature.trainingsList.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.BluetoothDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi

class TrainingsListScreenViewModel : ViewModel() {

    private val usbDeviceInteractor by diApi(UsbDeviceInteractorDiApi::usbDeviceInteractor)
    private val bleDeviceInteractor by diApi(BluetoothDeviceInteractorDiApi::bluetoothDeviceInteractor)
    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _sensorDeviceType = MutableStateFlow<SensorDeviceType?>(null)

    val sensorDeviceType: StateFlow<SensorDeviceType?> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach {
                _sensorDeviceType.emit(it)
            }
            .launchIn(viewModelScope)
    }

    fun isUsbDeviceAccessGranted(
        usbManager: UsbManager,
    ): Boolean {
        val device = usbManager.deviceList.values.lastOrNull()
        return device != null && usbManager.hasPermission(device)
    }

    fun hasConnectedDevices(usbManager: UsbManager) = usbManager.deviceList.isNotEmpty()

    fun connectToUsbDevice(usbManager: UsbManager) {
        usbDeviceInteractor.connectToUsbDevice(usbManager)
    }

    fun disconnect() {
        when (_sensorDeviceType.value) {
            SensorDeviceType.USB -> usbDeviceInteractor.disconnect()
            SensorDeviceType.BLUETOOTH -> bleDeviceInteractor.disconnect()
            SensorDeviceType.UNKNOWN, null -> {}
        }
    }

    private companion object {
        val TAG: String = TrainingsListScreenViewModel::class.java.simpleName
    }
}
