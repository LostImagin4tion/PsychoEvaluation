package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Component
import ru.miem.psychoEvaluation.multiplatform.core.di.AuthorizationRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.di.AuthorizationRepositoryModule

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
