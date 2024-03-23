package ru.miem.psychoEvaluation.feature.trainings.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.feature.trainings.api.TrainingsScreen

interface TrainingsScreenApi : Api {
    val trainingsScreen: TrainingsScreen
}