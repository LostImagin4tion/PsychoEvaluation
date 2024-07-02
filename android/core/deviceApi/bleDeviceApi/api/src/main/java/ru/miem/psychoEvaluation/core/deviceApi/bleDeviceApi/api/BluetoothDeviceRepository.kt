package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api

import android.bluetooth.le.BluetoothLeScanner
import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.core.deviceApi.api.DeviceRepository
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models.BluetoothDevice

interface BluetoothDeviceRepository : DeviceRepository {
    val devicesFlow: Flow<BluetoothDevice>

    fun scanForDevices(scanner: BluetoothLeScanner)
    fun connectToBluetoothDevice()
}
