package ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.DebugTrainingScreen

interface DebugTrainingScreenApi : Api {
    val debugTrainingScreen: DebugTrainingScreen
}
