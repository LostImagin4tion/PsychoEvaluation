package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen

interface AirplaneGameScreenDiApi : DiApi {
    val airplaneGameScreen: AirplaneGameScreen
}
