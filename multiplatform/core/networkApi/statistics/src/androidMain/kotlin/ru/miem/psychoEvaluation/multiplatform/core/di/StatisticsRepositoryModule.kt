package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.multiplatform.core.StatisticsRepository
import ru.miem.psychoEvaluation.multiplatform.core.StatisticsRepositoryImpl

@Module
interface StatisticsRepositoryModule {

    @Binds
    fun bindStatisticsRepository(impl: StatisticsRepositoryImpl): StatisticsRepository
}
