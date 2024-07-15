package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.BluetoothDeviceManagerScreenImpl

@Module
interface BluetoothDeviceManagerScreenModule {

    @Binds
    fun bindDeviceManagerScreen(impl: BluetoothDeviceManagerScreenImpl): BluetoothDeviceManagerScreen
}
