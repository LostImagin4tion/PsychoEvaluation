package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state

import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDevice

data class BluetoothDeviceState(
    val name: String? = null,
    val hardwareAddress: String,
    val connectionStatus: BluetoothDeviceConnectionStatus
)

enum class BluetoothDeviceConnectionStatus {
    Unknown,
    InProgress,
    Connected
}

fun BluetoothDevice.toDeviceState(): BluetoothDeviceState = BluetoothDeviceState(
    name = name,
    hardwareAddress = hardwareAddress,
    connectionStatus = BluetoothDeviceConnectionStatus.Unknown,
)
