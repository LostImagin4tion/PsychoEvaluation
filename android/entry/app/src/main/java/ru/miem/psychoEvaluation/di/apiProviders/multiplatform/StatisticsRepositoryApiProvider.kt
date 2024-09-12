package ru.miem.psychoEvaluation.di.apiProviders.multiplatform

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryComponent
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryDiApi

@Module
class StatisticsRepositoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(StatisticsRepositoryDiApi::class)
    fun provideStatisticsRepositoryApiProvider() =
        ApiProvider(StatisticsRepositoryComponent::create)
}
