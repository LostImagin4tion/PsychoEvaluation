package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDeviceData

interface BluetoothDeviceInteractor {
    val gsrBreathing: List<Int>
    val devicesFlow: Flow<BluetoothDevice>

    suspend fun findDataBorders(onCompleted: () -> Unit = {})

    suspend fun getRawDeviceData(onNewValueEmitted: suspend (Int) -> Unit)
    suspend fun getDeviceData(onNewValueEmitted: suspend (BluetoothDeviceData) -> Unit)

    fun scanForDevices(bluetoothScanner: BluetoothLeScanner)
    fun connectToBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        deviceHardwareAddress: String,
        onDeviceConnected: () -> Unit = {},
    )
    fun disconnect()

    fun increaseGameDifficulty()
    fun decreaseGameDifficulty()
    fun changeDataBorders(upperLimit: Double?, lowerLimit: Double?)
}
