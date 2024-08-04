package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.di.TrainingPreparingDiApi

@Module
class TrainingPreparingScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(TrainingPreparingDiApi::class)
    fun provideTrainingPreparingScreenApiProvider() =
        ApiProvider(TrainingPreparingScreenComponent::create)
}
