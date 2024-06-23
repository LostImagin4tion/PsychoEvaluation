package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.BluetoothDeviceRepository
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.BluetoothDeviceRepositoryImpl

@Module
interface BluetoothDeviceRepositoryModule {

    @Binds
    fun bindBluetoothDeviceRepository(impl: BluetoothDeviceRepositoryImpl): BluetoothDeviceRepository
}