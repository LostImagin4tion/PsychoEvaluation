package ru.miem.psychoEvaluation.feature.trainings.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.api.di.TrainingsScreenApi

@Component(
    modules = [
        TrainingsScreenModule::class,
    ]
)
interface TrainingsScreenComponent: TrainingsScreenApi {
    companion object {
        fun create(): TrainingsScreenApi = DaggerTrainingsScreenComponent.builder().build()
    }
}