package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.di.StatisticsInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.AuthorizationInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.BluetoothDeviceInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.DataAnalysisApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.RegistrationInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.SettingsInteractorApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.common.UsbDeviceInteractorApiProvider
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
