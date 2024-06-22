package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di.UsbDeviceRepositoryDiApi

@Module
class UsbDeviceRepositoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(UsbDeviceRepositoryDiApi::class)
    fun provideUsbDeviceRepositoryApiProvider() = ApiProvider(UsbDeviceRepositoryComponent::create)
}
