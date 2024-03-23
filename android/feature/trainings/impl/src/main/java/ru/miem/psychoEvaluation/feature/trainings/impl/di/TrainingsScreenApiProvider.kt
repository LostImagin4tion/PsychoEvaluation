package ru.miem.psychoEvaluation.feature.trainings.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.api.di.TrainingsScreenApi

@Module
class TrainingsScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(TrainingsScreenApi::class)
    fun provideTrainingsScreenApiProvider() = ApiProvider(TrainingsScreenComponent::create)
}