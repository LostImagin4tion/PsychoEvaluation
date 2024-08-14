package ru.miem.psychoEvaluation.di.apiProviders.android.common

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.di.AuthorizationInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.impl.di.AuthorizationInteractorComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class AuthorizationInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(AuthorizationInteractorDiApi::class)
    fun provideAuthorizationInteractorApiProvider() =
        ApiProvider(AuthorizationInteractorComponent.Companion::create)
}
