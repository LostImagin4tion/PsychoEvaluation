package ru.miem.psychoEvaluation.feature.trainingsList.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen

interface TrainingsScreenApi : Api {
    val trainingsListScreen: TrainingsListScreen
}
