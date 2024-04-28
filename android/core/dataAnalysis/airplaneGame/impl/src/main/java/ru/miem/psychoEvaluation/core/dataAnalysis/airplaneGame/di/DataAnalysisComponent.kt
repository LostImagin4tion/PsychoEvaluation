package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di

import dagger.Component
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisApi

@Component(
    modules = [
        DataAnalysisModule::class,
    ]
)
interface DataAnalysisComponent : DataAnalysisApi {
    companion object {
        fun create(): DataAnalysisApi = DaggerDataAnalysisComponent.builder().build()
    }
}