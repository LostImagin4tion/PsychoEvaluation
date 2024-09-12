package ru.miem.psychoEvaluation.di.modules

import dagger.Module
<<<<<<< HEAD
import ru.miem.psychoEvaluation.di.apiProviders.android.common.AuthorizationInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.BluetoothDeviceInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.DataAnalysisApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.RegistrationInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.SettingsInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.UsbDeviceInteractorApiProvider
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di.BluetoothDeviceInteractorApiProvider
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.di.UsbDeviceInteractorApiProvider
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.impl.di.AuthorizationInteractorApiProvider
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl.di.RegistrationInteractorApiProvider
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.di.StatisticsInteractorApiProvider
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl.di.SettingsInteractorApiProvider
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di.DataAnalysisApiProvider


@Module(
    includes = [
        DataAnalysisApiProvider::class,
        UsbDeviceInteractorApiProvider::class,
        BluetoothDeviceInteractorApiProvider::class,
        SettingsInteractorApiProvider::class,
        RegistrationInteractorApiProvider::class,
        AuthorizationInteractorApiProvider::class,
        StatisticsInteractorApiProvider::class,
    ]
)
interface CommonAggregationModule
