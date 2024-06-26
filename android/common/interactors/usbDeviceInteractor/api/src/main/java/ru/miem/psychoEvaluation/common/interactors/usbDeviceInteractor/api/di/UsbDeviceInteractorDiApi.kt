package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.di

import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface UsbDeviceInteractorDiApi : DiApi {

    val usbDeviceInteractor: UsbDeviceInteractor
}
