package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.di.RegistrationInteractorDiApi

@Component(
    modules = [
        RegistrationInteractorModule::class,
    ]
)
interface RegistrationInteractorComponent : RegistrationInteractorDiApi {
    companion object {
        fun create(): RegistrationInteractorDiApi = DaggerRegistrationInteractorComponent
            .builder()
            .build()
    }
}
