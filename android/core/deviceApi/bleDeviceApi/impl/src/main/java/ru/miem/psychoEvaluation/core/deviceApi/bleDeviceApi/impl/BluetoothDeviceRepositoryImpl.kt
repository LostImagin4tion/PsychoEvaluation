package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl

import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.BluetoothDeviceRepository
import javax.inject.Inject

class BluetoothDeviceRepositoryImpl @Inject constructor() : BluetoothDeviceRepository {

    override val deviceDataFlow: Flow<Int>
        get() = TODO("Not yet implemented")

    override val isConnected: Boolean
        get() = TODO("Not yet implemented")

    override fun connectToBluetoothDevice() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}