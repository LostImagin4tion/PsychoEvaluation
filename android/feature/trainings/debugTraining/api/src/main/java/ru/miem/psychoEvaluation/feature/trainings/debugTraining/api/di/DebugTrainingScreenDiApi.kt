package ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.DebugTrainingScreen

interface DebugTrainingScreenDiApi : DiApi {
    val debugTrainingScreen: DebugTrainingScreen
}
