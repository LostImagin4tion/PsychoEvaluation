package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.TrainingPreparingScreenImpl

@Module
interface TrainingPreparingScreenModule {

    @Binds
    fun bindTrainingPreparingScreen(impl: TrainingPreparingScreenImpl): TrainingPreparingScreen
}
