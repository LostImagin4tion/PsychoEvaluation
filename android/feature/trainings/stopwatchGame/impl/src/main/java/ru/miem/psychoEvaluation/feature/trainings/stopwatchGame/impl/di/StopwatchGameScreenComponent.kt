package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.di.StopwatchGameScreenDiApi

@Component(
    modules = [
        StopwatchGameScreenModule::class,
    ]
)
interface StopwatchGameScreenComponent : StopwatchGameScreenDiApi {
    companion object {
        fun create(): StopwatchGameScreenDiApi = DaggerStopwatchGameScreenComponent.builder().build()
    }
}
