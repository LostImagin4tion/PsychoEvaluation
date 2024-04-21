package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di.DebugTrainingScreenApi

@Component(
    modules = [
        DebugTrainingScreenModule::class,
    ]
)
interface DebugTrainingScreenComponent : DebugTrainingScreenApi {
    companion object {
        fun create(): DebugTrainingScreenApi = DaggerDebugTrainingScreenComponent.builder().build()
    }
}
