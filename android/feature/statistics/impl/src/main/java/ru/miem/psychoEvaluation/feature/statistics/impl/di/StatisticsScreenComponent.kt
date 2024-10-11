package ru.miem.psychoEvaluation.feature.statistics.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.statistics.api.di.StatisticsDiApi

@Component(
    modules = [
        StatisticsScreenModule::class,
    ],
)
interface StatisticsScreenComponent : StatisticsDiApi {
    companion object {
        fun create(): StatisticsDiApi = DaggerStatisticsScreenComponent.builder().build()
    }
}
