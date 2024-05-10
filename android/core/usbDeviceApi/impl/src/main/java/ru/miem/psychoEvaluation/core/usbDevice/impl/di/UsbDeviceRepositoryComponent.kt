package ru.miem.psychoEvaluation.core.usbDevice.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.core.usbDevice.api.di.UsbDeviceRepositoryDiApi

@Component(
    modules = [
        UsbDeviceRepositoryModule::class
    ]
)
interface UsbDeviceRepositoryComponent : UsbDeviceRepositoryDiApi {

    companion object {
        fun create(): UsbDeviceRepositoryDiApi = DaggerUsbDeviceRepositoryComponent.builder().build()
    }
}
