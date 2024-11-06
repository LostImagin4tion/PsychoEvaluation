package ru.miem.psychoEvaluation.di.apiProviders.android.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.di.BluetoothDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.di.BluetoothDeviceRepositoryComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class BluetoothDeviceRepositoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(BluetoothDeviceRepositoryDiApi::class)
    fun provideBluetoothDeviceRepositoryApiProvider() = ApiProvider(
        BluetoothDeviceRepositoryComponent.Companion::create
    )
}
