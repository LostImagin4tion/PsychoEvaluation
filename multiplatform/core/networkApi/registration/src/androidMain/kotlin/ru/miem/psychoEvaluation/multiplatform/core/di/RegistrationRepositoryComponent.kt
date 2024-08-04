package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Component

@Component(
    modules = [
        RegistrationRepositoryModule::class,
    ]
)
interface RegistrationRepositoryComponent : RegistrationRepositoryDiApi {
    companion object {
        fun create(): RegistrationRepositoryDiApi = DaggerRegistrationRepositoryComponent
            .builder()
            .build()
    }
}
