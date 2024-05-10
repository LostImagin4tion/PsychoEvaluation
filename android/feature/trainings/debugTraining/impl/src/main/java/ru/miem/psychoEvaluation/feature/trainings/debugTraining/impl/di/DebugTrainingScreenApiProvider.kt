package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di.DebugTrainingScreenDiApi

@Module
class DebugTrainingScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(DebugTrainingScreenDiApi::class)
    fun provideDebugTrainingScreenApiProvider() = ApiProvider(DebugTrainingScreenComponent::create)
}
