package ru.miem.psychoEvaluation.di.apiProviders.android.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di.UsbDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.di.UsbDeviceRepositoryComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class UsbDeviceRepositoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(UsbDeviceRepositoryDiApi::class)
    fun provideUsbDeviceRepositoryApiProvider() = ApiProvider(UsbDeviceRepositoryComponent.Companion::create)
}
