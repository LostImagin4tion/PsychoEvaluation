package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di.DataAnalysisApiProvider

@Module(
    includes = [
        DataAnalysisApiProvider::class,
    ]
)
interface CommonAggregationModule
