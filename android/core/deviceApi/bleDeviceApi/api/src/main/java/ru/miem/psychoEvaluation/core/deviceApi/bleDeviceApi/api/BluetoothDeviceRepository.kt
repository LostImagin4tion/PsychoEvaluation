package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.core.deviceApi.api.DeviceRepository

interface BluetoothDeviceRepository : DeviceRepository {
    fun connectToBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        deviceHardwareAddress: String,
        onDeviceConnected: () -> Unit,
    )
}
