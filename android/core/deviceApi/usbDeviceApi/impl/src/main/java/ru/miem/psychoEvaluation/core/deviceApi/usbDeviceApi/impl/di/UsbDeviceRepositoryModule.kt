package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.UsbDeviceRepository
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.UsbDeviceRepositoryImpl

@Module
interface UsbDeviceRepositoryModule {

    @Binds
    fun bindUsbDeviceRepository(impl: UsbDeviceRepositoryImpl): UsbDeviceRepository
}
