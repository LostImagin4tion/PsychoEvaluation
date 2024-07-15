package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state

import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDevice

data class BluetoothDeviceState(
    val name: String? = null,
    val hardwareAddress: String,
    val connectionStatus: BluetoothDeviceConnectionStatus
)

enum class BluetoothDeviceConnectionStatus {
    UNKNOWN,
    IN_PROGRESS,
    CONNECTED
}

fun BluetoothDevice.toDeviceState(): BluetoothDeviceState = BluetoothDeviceState(
    name = name,
    hardwareAddress = hardwareAddress,
    connectionStatus = BluetoothDeviceConnectionStatus.UNKNOWN,
)
