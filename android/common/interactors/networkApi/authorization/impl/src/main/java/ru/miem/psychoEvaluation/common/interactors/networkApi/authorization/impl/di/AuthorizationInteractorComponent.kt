package ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.di.AuthorizationInteractorDiApi

@Component(
    modules = [
        AuthorizationInteractorModule::class,
    ]
)
interface AuthorizationInteractorComponent : AuthorizationInteractorDiApi {
    companion object {
        fun create(): AuthorizationInteractorDiApi = DaggerAuthorizationInteractorComponent
            .builder()
            .build()
    }
}
