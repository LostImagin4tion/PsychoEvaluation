package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.api.ClocksGameScreen
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ClocksGameScreenImpl

@Module
interface ClocksGameScreenModule {

    @Binds
    fun bindStopwatchGameScreen(impl: ClocksGameScreenImpl): ClocksGameScreen
}
