package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.StatisticsInteractorImpl

@Module
interface StatisticsInteractorModule {

    @Binds
    fun bindStatisticsInteractor(impl: StatisticsInteractorImpl): StatisticsInteractor
}
