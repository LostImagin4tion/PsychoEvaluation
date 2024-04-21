package ru.miem.psychoEvaluation.core.usbDevice.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.core.usbDevice.api.UsbDeviceRepository
import ru.miem.psychoEvaluation.core.usbDevice.impl.UsbDeviceRepositoryImpl

@Module
interface UsbDeviceRepositoryModule {

    @Binds
    fun provideUsbDeviceRepository(impl: UsbDeviceRepositoryImpl): UsbDeviceRepository
}
