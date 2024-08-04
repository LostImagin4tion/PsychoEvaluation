package ru.miem.psychoEvaluation.feature.trainingPreparing.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen

interface TrainingPreparingDiApi : DiApi {
    val trainingPreparingScreen: TrainingPreparingScreen
}
