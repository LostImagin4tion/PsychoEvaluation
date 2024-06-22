package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.DataAnalysisImpl
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.DataAnalysis

@Module
interface DataAnalysisModule {

    @Binds
    fun bindDataAnalysis(impl: DataAnalysisImpl): DataAnalysis
}
