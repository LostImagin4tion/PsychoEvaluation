package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.di.AirplaneGameScreenDiApi
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.di.AirplaneGameScreenComponent

@Module
class AirplaneGameScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(AirplaneGameScreenDiApi::class)
    fun provideAirplaneGameScreenApiProvider() = ApiProvider(AirplaneGameScreenComponent.Companion::create)
}
