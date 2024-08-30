package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.di.AirplaneGameScreenDiApi

@Component(
    modules = [
        AirplaneGameScreenModule::class,
    ]
)
interface AirplaneGameScreenComponent : AirplaneGameScreenDiApi {
    companion object {
        fun create(): AirplaneGameScreenDiApi = DaggerAirplaneGameScreenComponent.builder().build()
    }
}
