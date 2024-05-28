package ru.miem.psychoEvaluation.feature.trainingsList.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainingsList.api.di.TrainingsScreenDiApi

@Module
class TrainingsListScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(TrainingsScreenDiApi::class)
    fun provideTrainingsListScreenApiProvider() = ApiProvider(TrainingsListScreenComponent::create)
}
