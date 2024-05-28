package ru.miem.psychoEvaluation.feature.trainingsList.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen

interface TrainingsScreenDiApi : DiApi {
    val trainingsListScreen: TrainingsListScreen
}
