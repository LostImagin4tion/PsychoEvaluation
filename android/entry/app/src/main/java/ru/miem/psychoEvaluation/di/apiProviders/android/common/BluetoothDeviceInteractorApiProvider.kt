package ru.miem.psychoEvaluation.di.apiProviders.android.common

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.BluetoothDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di.BluetoothDeviceInteractorComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class BluetoothDeviceInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(BluetoothDeviceInteractorDiApi::class)
    fun provideBluetoothDeviceInteractorApiProvider() =
        ApiProvider(BluetoothDeviceInteractorComponent.Companion::create)
}
