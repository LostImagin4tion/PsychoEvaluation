package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di.UsbDeviceRepositoryDiApi

@Component(
    modules = [
        UsbDeviceRepositoryModule::class
    ]
)
interface UsbDeviceRepositoryComponent :
    UsbDeviceRepositoryDiApi {

    companion object {
        fun create(): UsbDeviceRepositoryDiApi = DaggerUsbDeviceRepositoryComponent.builder().build()
    }
}
