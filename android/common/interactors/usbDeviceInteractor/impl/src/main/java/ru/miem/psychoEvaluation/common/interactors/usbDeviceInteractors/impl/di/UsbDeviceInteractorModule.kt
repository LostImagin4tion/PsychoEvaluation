package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.impl.UsbDeviceInteractorImpl

@Module
interface UsbDeviceInteractorModule {

    @Binds
    fun bindUsbDeviceInteractor(impl: UsbDeviceInteractorImpl): UsbDeviceInteractor
}
