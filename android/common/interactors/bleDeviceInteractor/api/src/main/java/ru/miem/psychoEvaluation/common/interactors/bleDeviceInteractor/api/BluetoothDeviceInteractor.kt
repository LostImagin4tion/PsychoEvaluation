package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDeviceData

interface BluetoothDeviceInteractor {
    val devicesFlow: Flow<BluetoothDevice>

    suspend fun findDataBorders(onCompleted: () -> Unit)

    suspend fun getAllRawDeviceData(onNewValueEmitted: suspend (List<Int>) -> Unit)
    suspend fun getRawDeviceData(onNewValueEmitted: suspend (Int) -> Unit)
    suspend fun getNormalizedDeviceData(
        normalizationFactor: Double,
        onNewValueEmitted: suspend (BluetoothDeviceData) -> Unit
    )

    fun scanForDevices(bluetoothScanner: BluetoothLeScanner)
    fun connectToBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        deviceHardwareAddress: String,
        onDeviceConnected: () -> Unit,
    )
    fun disconnect()
}
