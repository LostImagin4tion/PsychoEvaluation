package ru.miem.psychoEvaluation.feature.trainings.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainings.api.TrainingsScreen
import ru.miem.psychoEvaluation.feature.trainings.impl.TrainingsScreenImpl

@Module
interface TrainingsScreenModule {

    @Binds
    fun provideTrainingsScreen(impl: TrainingsScreenImpl): TrainingsScreen
}