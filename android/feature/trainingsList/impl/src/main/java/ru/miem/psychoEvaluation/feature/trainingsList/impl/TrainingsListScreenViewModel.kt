package ru.miem.psychoEvaluation.feature.trainingsList.impl

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi

class TrainingsListScreenViewModel : ViewModel() {

    private val usbDeviceInteractor by diApi(UsbDeviceInteractorDiApi::usbDeviceInteractor)
    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.UNKNOWN)

    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        viewModelScope.launch {
            settingsInteractor.getCurrentSensorDeviceType().collect {
                _sensorDeviceType.emit(it)
            }
        }
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

    fun disconnect() = usbDeviceInteractor.disconnect()

    @SuppressLint("MissingPermission")
    fun scanBluetoothDevices(
        bluetoothScanner: BluetoothLeScanner
    ) {
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                Log.d("HELLO", "FOUND device ${result?.device?.name}")
            }
        }

        viewModelScope.launch {
            bluetoothScanner.startScan(scanCallback)
        }
    }

    private companion object {
        val TAG: String = TrainingsListScreenViewModel::class.java.simpleName
    }
}