package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di.DebugTrainingScreenDiApi
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.di.DebugTrainingScreenComponent

@Module
class DebugTrainingScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(DebugTrainingScreenDiApi::class)
    fun provideDebugTrainingScreenApiProvider() = ApiProvider(DebugTrainingScreenComponent.Companion::create)
}
