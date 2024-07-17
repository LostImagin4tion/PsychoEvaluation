package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.multiplatform.core.AuthorizationRepository
import ru.miem.psychoEvaluation.multiplatform.core.AuthorizationRepositoryImpl

@Module
interface AuthorizationRepositoryModule {

    @Binds
    fun bindAuthorizationRepository(impl: AuthorizationRepositoryImpl): AuthorizationRepository
}