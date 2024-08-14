package ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.multiplatform.core.di.AuthorizationRepositoryComponent
import ru.miem.psychoEvaluation.multiplatform.core.di.AuthorizationRepositoryDiApi

@Module
class AuthorizationRepositoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(AuthorizationRepositoryDiApi::class)
    fun provideAuthorizationRepositoryApiProvider() =
        ApiProvider(AuthorizationRepositoryComponent::create)
}
