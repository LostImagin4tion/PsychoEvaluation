package ru.miem.psychoEvaluation.feature.trainings.clocksGame.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.api.ClocksGameScreen

interface ClocksGameScreenDiApi : DiApi {
    val clocksGameScreen: ClocksGameScreen
}
