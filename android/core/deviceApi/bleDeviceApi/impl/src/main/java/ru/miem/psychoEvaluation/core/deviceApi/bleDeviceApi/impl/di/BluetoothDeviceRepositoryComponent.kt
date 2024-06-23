package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.di.BluetoothDeviceRepositoryDiApi

@Component(
    modules = [
        BluetoothDeviceRepositoryModule::class,
    ]
)
interface BluetoothDeviceRepositoryComponent : BluetoothDeviceRepositoryDiApi {

    companion object {
        fun create(): BluetoothDeviceRepositoryDiApi = DaggerBluetoothDeviceRepositoryComponent.builder().build()
    }
}