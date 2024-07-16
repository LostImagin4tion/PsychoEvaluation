package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.BluetoothDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothAdvertisementStatus
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceConnectionStatus
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceState
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.toDeviceState

class BluetoothDeviceManagerViewModel : ViewModel() {

    private val bleDeviceInteractor by diApi(BluetoothDeviceInteractorDiApi::bluetoothDeviceInteractor)

    private val _devices =
        MutableStateFlow<ImmutableMap<String, BluetoothDeviceState>>(persistentHashMapOf())

    val devices: Flow<ImmutableList<BluetoothDeviceState>> = _devices
        .map { it.values.toImmutableList() }

    fun discoverBluetoothDevices(
        bluetoothScanner: BluetoothLeScanner
    ) {
        viewModelScope.launch {
            bleDeviceInteractor.scanForDevices(bluetoothScanner)
            bleDeviceInteractor.devicesFlow.collect { device ->
                val newDevice = device.toDeviceState()

                val newMap = _devices.value
                    .toPersistentHashMap()
                    .mutate {
                        when (device.advertisementStatus) {
                            BluetoothAdvertisementStatus.Available -> {
                                it[device.hardwareAddress] = newDevice
                            }

                            BluetoothAdvertisementStatus.NotAvailable,
                            null
                            -> it.remove(device.hardwareAddress)
                        }
                    }
                    .toImmutableMap()

                _devices.emit(newMap)
            }
        }
    }

    fun connectToBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        deviceState: BluetoothDeviceState,
        onDeviceConnected: () -> Unit = {}
    ) {
        changeDeviceConnectionStatus(deviceState, BluetoothDeviceConnectionStatus.InProgress)
        bleDeviceInteractor.connectToBluetoothDevice(
            activity,
            bluetoothAdapter,
            deviceState.hardwareAddress,
        ) {
            changeDeviceConnectionStatus(deviceState, BluetoothDeviceConnectionStatus.Connected)
            onDeviceConnected()
        }
    }

    fun disconnectBluetoothDevice() = bleDeviceInteractor.disconnect()

    private fun changeDeviceConnectionStatus(
        deviceState: BluetoothDeviceState,
        newStatus: BluetoothDeviceConnectionStatus
    ) {
        viewModelScope.launch {
            val newMap = _devices.value
                .toPersistentHashMap()
                .mutate { map ->
                    map[deviceState.hardwareAddress]
                        ?.let {
                            map[deviceState.hardwareAddress] = it.copy(connectionStatus = newStatus)
                        }
                }
                .toImmutableMap()

            _devices.emit(newMap)
        }
    }

    private companion object {
        val TAG: String = BluetoothDeviceManagerScreenImpl::class.java.name
    }
}
