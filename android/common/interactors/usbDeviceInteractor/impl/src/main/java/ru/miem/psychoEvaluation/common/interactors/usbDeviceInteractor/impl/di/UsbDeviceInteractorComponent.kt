package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.di.UsbDeviceInteractorDiApi

@Component(
    modules = [
        UsbDeviceInteractorModule::class
    ]
)
interface UsbDeviceInteractorComponent : UsbDeviceInteractorDiApi {
    companion object {
        fun create(): UsbDeviceInteractorDiApi = DaggerUsbDeviceInteractorComponent.builder().build()
    }
}
