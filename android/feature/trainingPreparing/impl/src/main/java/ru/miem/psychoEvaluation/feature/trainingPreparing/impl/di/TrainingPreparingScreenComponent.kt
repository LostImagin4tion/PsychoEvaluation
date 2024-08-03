package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.di.TrainingPreparingDiApi

@Component(
    modules = [
        TrainingPreparingScreenModule::class,
    ]
)
interface TrainingPreparingScreenComponent : TrainingPreparingDiApi {
    companion object {
        fun create(): TrainingPreparingDiApi = DaggerTrainingPreparingScreenComponent
            .builder()
            .build()
    }
}