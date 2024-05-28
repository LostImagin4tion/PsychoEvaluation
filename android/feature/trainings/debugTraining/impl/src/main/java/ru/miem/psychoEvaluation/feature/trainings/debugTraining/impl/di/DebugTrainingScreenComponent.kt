package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di.DebugTrainingScreenDiApi

@Component(
    modules = [
        DebugTrainingScreenModule::class,
    ]
)
interface DebugTrainingScreenComponent : DebugTrainingScreenDiApi {
    companion object {
        fun create(): DebugTrainingScreenDiApi = DaggerDebugTrainingScreenComponent.builder().build()
    }
}
