package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.core.di.impl.diApi

class AirplaneGameScreenViewModel : ViewModel() {

    private val usbDeviceInteractor by diApi(UsbDeviceInteractorDiApi::usbDeviceInteractor)
    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _stressData = MutableStateFlow(UsbDeviceData(0, 0.0))
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.UNKNOWN)

    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType
    val stressData: StateFlow<UsbDeviceData> = _stressData

    val allStress = mutableListOf<Int>()
    val chartModelProducer = CartesianChartModelProducer.build()

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

    fun connectToUsbDevice(usbManager: UsbManager, screenHeight: Double) {
        viewModelScope.launch {
            usbDeviceInteractor.getNormalizedDeviceData(usbManager, screenHeight) {
                _stressData.emit(it)
                allStress.add(it.rawData)
                chartModelProducer.runTransaction {
                    lineSeries { series(allStress) }
                }
            }
        }
    }

    fun disconnect() = usbDeviceInteractor.disconnect()

    @SuppressLint("MissingPermission")
    fun scanBluetoothDevices(
        bluetoothScanner: BluetoothLeScanner
    ) {
        val scanCallback = object: ScanCallback() {
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
        val TAG: String = AirplaneGameScreenViewModel::class.java.simpleName
    }
}
