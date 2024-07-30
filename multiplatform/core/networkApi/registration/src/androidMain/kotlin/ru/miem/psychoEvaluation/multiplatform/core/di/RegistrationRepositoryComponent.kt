package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Component

@Component(
    modules = [
        AuthorizationRepositoryModule::class,
    ]
)
interface AuthorizationRepositoryComponent : AuthorizationRepositoryDiApi {
    companion object {
        fun create(): AuthorizationRepositoryDiApi = DaggerAuthorizationRepositoryComponent
            .builder()
            .build()
    }
}
