package ru.miem.psychoEvaluation.feature.authorization.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationApi

@Component(
    modules = [
        AuthorizationScreenModule::class
    ],
)
interface AuthorizationScreenComponent : AuthorizationApi {
    companion object {
        fun create(): AuthorizationApi = DaggerAuthorizationScreenComponent.builder().build()
    }
}
