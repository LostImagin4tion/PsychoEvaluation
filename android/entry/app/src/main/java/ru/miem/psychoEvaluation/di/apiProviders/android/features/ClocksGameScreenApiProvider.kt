package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.di.StopwatchGameScreenDiApi
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.di.StopwatchGameScreenComponent

@Module
class StopwatchGameScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(StopwatchGameScreenDiApi::class)
    fun provideStopwatchGameScreenApiProvider() = ApiProvider(StopwatchGameScreenComponent.Companion::create)
}
