package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di

import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface BluetoothDeviceInteractorDiApi : DiApi {

    val bluetoothDeviceInteractor: BluetoothDeviceInteractor
}