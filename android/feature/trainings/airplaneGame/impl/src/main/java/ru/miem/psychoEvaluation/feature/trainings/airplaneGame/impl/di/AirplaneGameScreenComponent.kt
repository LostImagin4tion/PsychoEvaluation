package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreenApi

@Component(
    modules = [
        AirplaneGameScreenModule::class,
    ]
)
interface AirplaneGameScreenComponent : AirplaneGameScreenApi {
    companion object {
        fun create(): AirplaneGameScreenApi = DaggerAirplaneGameScreenComponent.builder().build()
    }
}