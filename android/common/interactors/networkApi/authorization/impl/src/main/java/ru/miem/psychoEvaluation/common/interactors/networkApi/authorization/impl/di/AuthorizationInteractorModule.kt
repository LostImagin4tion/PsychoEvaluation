package ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.AuthorizationInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.impl.AuthorizationInteractorImpl

@Module
interface AuthorizationInteractorModule {

    @Binds
    fun bindAuthorizationInteractor(impl: AuthorizationInteractorImpl): AuthorizationInteractor
}
