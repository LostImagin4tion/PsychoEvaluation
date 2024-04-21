package ru.miem.psychoEvaluation.feature.trainingsList.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen
import ru.miem.psychoEvaluation.feature.trainingsList.impl.TrainingsListScreenImpl

@Module
interface TrainingsListScreenModule {

    @Binds
    fun provideTrainingsListScreen(impl: TrainingsListScreenImpl): TrainingsListScreen
}
