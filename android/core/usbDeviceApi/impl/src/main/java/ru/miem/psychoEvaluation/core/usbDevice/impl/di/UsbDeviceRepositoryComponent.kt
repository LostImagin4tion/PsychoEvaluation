package ru.miem.psychoEvaluation.core.usbDevice.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.core.usbDevice.api.di.UsbDeviceRepositoryApi

@Component(
    modules = [
        UsbDeviceRepositoryModule::class
    ]
)
interface UsbDeviceRepositoryComponent : UsbDeviceRepositoryApi {

    companion object {
        fun create(): UsbDeviceRepositoryApi = DaggerUsbDeviceRepositoryComponent.builder().build()
    }
}
