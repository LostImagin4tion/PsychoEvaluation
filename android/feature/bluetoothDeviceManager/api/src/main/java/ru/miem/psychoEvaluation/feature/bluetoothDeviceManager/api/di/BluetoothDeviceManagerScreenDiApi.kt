package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen

interface BluetoothDeviceManagerScreenDiApi : DiApi {
    val bluetoothDeviceManagerScreen: BluetoothDeviceManagerScreen
}
