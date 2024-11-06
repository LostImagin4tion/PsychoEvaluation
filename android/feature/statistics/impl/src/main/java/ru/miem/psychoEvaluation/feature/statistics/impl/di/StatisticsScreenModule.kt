package ru.miem.psychoEvaluation.feature.statistics.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.statistics.api.StatisticsScreen
import ru.miem.psychoEvaluation.feature.statistics.impl.StatisticsScreenImpl

@Module
interface StatisticsScreenModule {

    @Binds
    fun bindStatisticsScreen(impl: StatisticsScreenImpl): StatisticsScreen
}
