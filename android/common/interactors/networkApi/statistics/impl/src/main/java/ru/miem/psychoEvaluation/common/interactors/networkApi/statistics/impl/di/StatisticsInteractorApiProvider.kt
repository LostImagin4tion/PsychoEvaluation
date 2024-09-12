package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class StatisticsInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(StatisticsInteractorDiApi::class)
    fun provideStatisticsInteractorApiProvider() =
        ApiProvider(StatisticsInteractorComponent::create)
}
