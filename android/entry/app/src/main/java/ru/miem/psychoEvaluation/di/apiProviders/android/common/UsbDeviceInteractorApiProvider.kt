package ru.miem.psychoEvaluation.di.apiProviders.android.common

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di.UsbDeviceInteractorComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class UsbDeviceInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(UsbDeviceInteractorDiApi::class)
    fun provideUsbDeviceInteractorApiProvider() = ApiProvider(UsbDeviceInteractorComponent.Companion::create)
}
