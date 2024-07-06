package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl

import android.bluetooth.le.BluetoothLeScanner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.di.BluetoothDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models.BluetoothDeviceStatus
import ru.miem.psychoEvaluation.core.di.impl.diApi

class BluetoothDeviceManagerViewModel : ViewModel() {

    private val bluetoothDeviceRepository by diApi(BluetoothDeviceRepositoryDiApi::bluetoothDeviceRepository)

    private val _devices = MutableStateFlow<Map<String, BluetoothDevice>>(hashMapOf())

    val devices: StateFlow<Map<String, BluetoothDevice>> = _devices

    fun discoverBluetoothDevices(
        bluetoothScanner: BluetoothLeScanner
    ) {
        viewModelScope.launch {
            bluetoothDeviceRepository.scanForDevices(bluetoothScanner)
            bluetoothDeviceRepository.devicesFlow.collect { device ->
                val newMap = _devices.value.toMutableMap().apply {
                    when (device.status) {
                        BluetoothDeviceStatus.AVAILABLE,
                        BluetoothDeviceStatus.CONNECTED
                        -> put(device.hardwareAddress, device)

                        BluetoothDeviceStatus.NOT_AVAILABLE -> remove(device.hardwareAddress)
                    }
                }
                _devices.emit(newMap)
            }
        }
    }

    private companion object {
        val TAG: String = BluetoothDeviceManagerScreenImpl::class.java.name
    }
}