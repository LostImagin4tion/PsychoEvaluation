package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreenApi

@Module
class AirplaneGameScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(AirplaneGameScreenApi::class)
    fun provideAirplaneGameScreenApiProvider() = ApiProvider(AirplaneGameScreenComponent::create)
}
