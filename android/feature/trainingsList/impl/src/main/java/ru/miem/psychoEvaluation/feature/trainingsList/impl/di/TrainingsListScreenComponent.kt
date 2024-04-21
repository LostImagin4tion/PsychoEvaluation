package ru.miem.psychoEvaluation.feature.trainingsList.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainingsList.api.di.TrainingsScreenApi

@Component(
    modules = [
        TrainingsListScreenModule::class,
    ]
)
interface TrainingsListScreenComponent : TrainingsScreenApi {
    companion object {
        fun create(): TrainingsScreenApi = DaggerTrainingsListScreenComponent.builder().build()
    }
}
