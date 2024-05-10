package ru.miem.psychoEvaluation.feature.authorization.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationDiApi

@Component(
    modules = [
        AuthorizationScreenModule::class
    ],
)
interface AuthorizationScreenComponent : AuthorizationDiApi {
    companion object {
        fun create(): AuthorizationDiApi = DaggerAuthorizationScreenComponent.builder().build()
    }
}
