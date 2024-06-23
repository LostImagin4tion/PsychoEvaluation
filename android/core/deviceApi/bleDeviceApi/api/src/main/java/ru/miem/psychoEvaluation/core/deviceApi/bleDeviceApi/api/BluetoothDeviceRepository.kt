package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api

import ru.miem.psychoEvaluation.core.deviceApi.api.DeviceRepository

interface BluetoothDeviceRepository : DeviceRepository {
    fun connectToBluetoothDevice()
}