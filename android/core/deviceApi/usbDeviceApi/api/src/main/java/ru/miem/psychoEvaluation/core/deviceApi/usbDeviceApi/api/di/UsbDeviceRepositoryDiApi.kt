package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di

import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.UsbDeviceRepository
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface UsbDeviceRepositoryDiApi : DiApi {

    val usbDeviceRepository: UsbDeviceRepository
}
