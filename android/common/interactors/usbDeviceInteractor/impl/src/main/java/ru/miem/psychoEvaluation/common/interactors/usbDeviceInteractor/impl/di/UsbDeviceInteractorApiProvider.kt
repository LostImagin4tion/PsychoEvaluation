package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class UsbDeviceInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(UsbDeviceInteractorDiApi::class)
    fun provideUsbDeviceInteractorApiProvider() = ApiProvider(UsbDeviceInteractorComponent::create)
}
