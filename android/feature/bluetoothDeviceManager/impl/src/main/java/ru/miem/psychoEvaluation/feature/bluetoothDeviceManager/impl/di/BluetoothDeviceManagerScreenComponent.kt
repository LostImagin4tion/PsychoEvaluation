package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.di.BluetoothDeviceManagerScreenDiApi

@Component(
    modules = [
        BluetoothDeviceManagerScreenModule::class,
    ]
)
interface BluetoothDeviceManagerScreenComponent : BluetoothDeviceManagerScreenDiApi {
    companion object {
        fun create(): BluetoothDeviceManagerScreenDiApi =
            DaggerBluetoothDeviceManagerScreenComponent.builder().build()
    }
}
