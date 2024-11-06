package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.StopwatchGameScreen
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.StopwatchGameScreenImpl

@Module
interface StopwatchGameScreenModule {

    @Binds
    fun bindStopwatchGameScreen(impl: StopwatchGameScreenImpl): StopwatchGameScreen
}
