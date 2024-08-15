package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.di.BluetoothDeviceManagerScreenDiApi
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.di.BluetoothDeviceManagerScreenComponent

@Module
class BluetoothDeviceManagerScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(BluetoothDeviceManagerScreenDiApi::class)
    fun provideDeviceManagerScreenApiProvider() = ApiProvider(BluetoothDeviceManagerScreenComponent.Companion::create)
}
