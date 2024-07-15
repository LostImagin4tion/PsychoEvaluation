package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di

import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface UsbDeviceInteractorDiApi : DiApi {

    val usbDeviceInteractor: UsbDeviceInteractor
}
