package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.api.di.ClocksGameScreenDiApi

@Component(
    modules = [
        ClocksGameScreenModule::class,
    ]
)
interface ClocksGameScreenComponent : ClocksGameScreenDiApi {
    companion object {
        fun create(): ClocksGameScreenDiApi = DaggerClocksGameScreenComponent.builder().build()
    }
}
