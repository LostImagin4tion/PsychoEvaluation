package ru.miem.psychoEvaluation.feature.registration.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationApi

@Component(
    modules = [
        RegistrationScreenModule::class
    ],
)
interface RegistrationScreenComponent : RegistrationApi {
    companion object {
        fun create(): RegistrationApi = DaggerRegistrationScreenComponent.builder().build()
    }
}
