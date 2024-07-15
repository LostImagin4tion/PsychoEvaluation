package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.BluetoothDeviceInteractorImpl

@Module
interface BluetoothDeviceInteractorModule {

    @Binds
    fun bindBluetoothDeviceInteractor(impl: BluetoothDeviceInteractorImpl): BluetoothDeviceInteractor
}