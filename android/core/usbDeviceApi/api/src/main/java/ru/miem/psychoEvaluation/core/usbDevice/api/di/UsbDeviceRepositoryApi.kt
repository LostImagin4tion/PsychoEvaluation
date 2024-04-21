package ru.miem.psychoEvaluation.core.usbDevice.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.core.usbDevice.api.UsbDeviceRepository

interface UsbDeviceRepositoryApi : Api {

    val usbDeviceRepository: UsbDeviceRepository
}
