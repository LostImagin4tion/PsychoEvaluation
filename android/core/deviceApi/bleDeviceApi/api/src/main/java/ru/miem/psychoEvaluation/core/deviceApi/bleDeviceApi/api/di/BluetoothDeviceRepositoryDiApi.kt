package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.di

import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.BluetoothDeviceRepository
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface BluetoothDeviceRepositoryDiApi : DiApi {
    val bluetoothDeviceRepository: BluetoothDeviceRepository
}