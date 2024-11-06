package ru.miem.psychoEvaluation.feature.registration.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationDiApi

@Component(
    modules = [
        RegistrationScreenModule::class
    ],
)
interface RegistrationScreenComponent : RegistrationDiApi {
    companion object {
        fun create(): RegistrationDiApi = DaggerRegistrationScreenComponent.builder().build()
    }
}
