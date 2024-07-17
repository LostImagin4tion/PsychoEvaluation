package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.BluetoothDeviceInteractorDiApi

@Component(
    modules = [
        BluetoothDeviceInteractorModule::class,
    ]
)
interface BluetoothDeviceInteractorComponent : BluetoothDeviceInteractorDiApi {
    companion object {
        fun create(): BluetoothDeviceInteractorDiApi = DaggerBluetoothDeviceInteractorComponent
            .builder()
            .build()
    }
}
