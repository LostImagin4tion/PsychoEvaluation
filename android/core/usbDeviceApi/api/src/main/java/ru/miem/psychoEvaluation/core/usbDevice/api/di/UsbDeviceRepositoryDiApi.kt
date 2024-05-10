package ru.miem.psychoEvaluation.core.usbDevice.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.core.usbDevice.api.UsbDeviceRepository

interface UsbDeviceRepositoryDiApi : DiApi {

    val usbDeviceRepository: UsbDeviceRepository
}
