package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.di.BluetoothDeviceManagerScreenDiApi

@Module
class BluetoothDeviceManagerScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(BluetoothDeviceManagerScreenDiApi::class)
    fun provideDeviceManagerScreenApiProvider() = ApiProvider(BluetoothDeviceManagerScreenComponent::create)
}
