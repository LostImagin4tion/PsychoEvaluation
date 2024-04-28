package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di

import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.DataAnalysis
import ru.miem.psychoEvaluation.core.di.api.Api

interface DataAnalysisApi : Api {
    val dataAnalysis: DataAnalysis
}