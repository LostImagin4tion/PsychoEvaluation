package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.AirplaneGameScreenImpl

@Module
interface AirplaneGameScreenModule {

    @Binds
    fun provideAirplaneGameScreen(impl: AirplaneGameScreenImpl): AirplaneGameScreen
}
