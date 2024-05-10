package ru.miem.psychoEvaluation.feature.trainingsList.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainingsList.api.di.TrainingsScreenDiApi

@Component(
    modules = [
        TrainingsListScreenModule::class,
    ]
)
interface TrainingsListScreenComponent : TrainingsScreenDiApi {
    companion object {
        fun create(): TrainingsScreenDiApi = DaggerTrainingsListScreenComponent.builder().build()
    }
}
