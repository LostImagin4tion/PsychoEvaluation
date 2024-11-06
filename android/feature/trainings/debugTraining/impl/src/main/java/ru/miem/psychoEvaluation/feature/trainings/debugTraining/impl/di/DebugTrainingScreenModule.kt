package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.DebugTrainingScreen
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.DebugTrainingScreenImpl

@Module
interface DebugTrainingScreenModule {

    @Binds
    fun bindDebugTrainingScreen(impl: DebugTrainingScreenImpl): DebugTrainingScreen
}
