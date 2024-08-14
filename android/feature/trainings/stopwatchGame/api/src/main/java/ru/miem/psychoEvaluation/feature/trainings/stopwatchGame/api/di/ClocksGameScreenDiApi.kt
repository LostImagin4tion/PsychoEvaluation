package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.StopwatchGameScreen

interface StopwatchGameScreenDiApi : DiApi {
    val stopwatchGameScreen: StopwatchGameScreen
}