package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.UsbDeviceRepository

interface UsbDeviceRepositoryDiApi : DiApi {

    val usbDeviceRepository: UsbDeviceRepository
}
