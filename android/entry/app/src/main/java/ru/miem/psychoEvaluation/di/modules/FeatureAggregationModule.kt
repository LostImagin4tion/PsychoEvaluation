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
import ru.miem.psychoEvaluation.feature.authorization.impl.di.AuthorizationScreenApiProvider
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.di.BluetoothDeviceManagerScreenApiProvider
import ru.miem.psychoEvaluation.feature.registration.impl.di.RegistrationScreenApiProvider
import ru.miem.psychoEvaluation.feature.statistics.impl.di.StatisticsScreenApiProvider
import ru.miem.psychoEvaluation.feature.settings.impl.di.SettingsScreenApiProvider
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.di.TrainingPreparingScreenApiProvider
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.di.AirplaneGameScreenApiProvider
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.di.DebugTrainingScreenApiProvider
import ru.miem.psychoEvaluation.feature.trainingsList.impl.di.TrainingsListScreenApiProvider
import ru.miem.psychoEvaluation.feature.userProfile.impl.di.UserProfileScreenApiProvider

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
