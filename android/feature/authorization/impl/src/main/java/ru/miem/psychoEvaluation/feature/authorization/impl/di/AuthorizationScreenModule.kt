package ru.miem.psychoEvaluation.feature.authorization.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.authorization.impl.AuthorizationScreenImpl

@Module
interface AuthorizationScreenModule {

    @Binds
    fun provideAuthorizationScreen(impl: AuthorizationScreenImpl): AuthorizationScreen
}
