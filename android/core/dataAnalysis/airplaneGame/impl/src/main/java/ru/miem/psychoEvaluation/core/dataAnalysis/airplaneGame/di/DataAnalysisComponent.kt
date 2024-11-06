package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di

import dagger.Component
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisDiApi

@Component(
    modules = [
        DataAnalysisModule::class,
    ]
)
interface DataAnalysisComponent : DataAnalysisDiApi {
    companion object {
        fun create(): DataAnalysisDiApi = DaggerDataAnalysisComponent.builder().build()
    }
}
