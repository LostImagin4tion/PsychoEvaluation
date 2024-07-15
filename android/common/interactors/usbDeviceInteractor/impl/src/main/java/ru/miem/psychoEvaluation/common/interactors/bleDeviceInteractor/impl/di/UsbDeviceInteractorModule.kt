package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.UsbDeviceInteractorImpl

@Module
interface UsbDeviceInteractorModule {

    @Binds
    fun bindUsbDeviceInteractor(impl: UsbDeviceInteractorImpl): UsbDeviceInteractor
}
