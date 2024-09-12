package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Component

@Component(
    modules = [
        StatisticsRepositoryModule::class,
    ]
)
interface StatisticsRepositoryComponent : StatisticsRepositoryDiApi {
    companion object {
        fun create(): StatisticsRepositoryDiApi = DaggerStatisticsRepositoryComponent
            .builder()
            .build()
    }
}
