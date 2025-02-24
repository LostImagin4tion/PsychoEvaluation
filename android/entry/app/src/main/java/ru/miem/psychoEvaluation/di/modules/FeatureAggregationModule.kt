package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.di.apiProviders.android.features.AirplaneGameScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.AuthorizationScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.BluetoothDeviceManagerScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.ClocksGameScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.DebugTrainingScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.RegistrationScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.SettingsScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.StopwatchGameScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.TrainingPreparingScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.TrainingsListScreenApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.features.UserProfileScreenApiProvider
import ru.miem.psychoEvaluation.feature.statistics.impl.di.StatisticsScreenApiProvider
@Module(
    includes = [
        AuthorizationScreenApiProvider::class,
        RegistrationScreenApiProvider::class,
        UserProfileScreenApiProvider::class,
        SettingsScreenApiProvider::class,
        BluetoothDeviceManagerScreenApiProvider::class,
        TrainingPreparingScreenApiProvider::class,
        TrainingsListScreenApiProvider::class,
        DebugTrainingScreenApiProvider::class,
        AirplaneGameScreenApiProvider::class,
        StopwatchGameScreenApiProvider::class,
        ClocksGameScreenApiProvider::class,
        StatisticsScreenApiProvider::class,
    ]
)
interface FeatureAggregationModule
