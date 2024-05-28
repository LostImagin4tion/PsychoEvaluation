package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisDiApi
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class DataAnalysisApiProvider {

    @Provides
    @IntoMap
    @ApiKey(DataAnalysisDiApi::class)
    fun provideDataAnalysisApiProvider() = ApiProvider(DataAnalysisComponent::create)
}
