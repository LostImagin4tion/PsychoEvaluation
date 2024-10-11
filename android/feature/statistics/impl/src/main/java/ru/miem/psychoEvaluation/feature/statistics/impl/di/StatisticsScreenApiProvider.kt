package ru.miem.psychoEvaluation.feature.statistics.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.statistics.api.di.StatisticsDiApi

@Module
class StatisticsScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(StatisticsDiApi::class)
    fun provideStatisticsScreenApiProvider() = ApiProvider(StatisticsScreenComponent::create)
}
