package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.impl.UsbDeviceInteractorImpl

@Module
interface UsbDeviceInteractorModule {

    @Binds
    fun bindUsbDeviceInteractor(impl: UsbDeviceInteractorImpl): UsbDeviceInteractor
}
