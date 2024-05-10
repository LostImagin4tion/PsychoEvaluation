package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.di

import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface UsbDeviceInteractorDiApi : DiApi {

    val usbDeviceInteractor: UsbDeviceInteractor
}