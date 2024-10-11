package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi

@Component(
    modules = [
        StatisticsInteractorModule::class,
    ]
)
interface StatisticsInteractorComponent : StatisticsInteractorDiApi {
    companion object {
        fun create(): StatisticsInteractorDiApi = DaggerStatisticsInteractorComponent
            .builder()
            .build()
    }
}
