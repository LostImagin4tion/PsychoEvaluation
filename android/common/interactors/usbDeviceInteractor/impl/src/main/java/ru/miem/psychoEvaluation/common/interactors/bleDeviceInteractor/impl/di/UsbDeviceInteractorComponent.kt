package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.UsbDeviceInteractorDiApi

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
