package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationDiApi
import ru.miem.psychoEvaluation.feature.authorization.impl.di.AuthorizationScreenComponent

@Module
class AuthorizationScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(AuthorizationDiApi::class)
    fun providesAuthorizationScreenApiProvider() = ApiProvider(AuthorizationScreenComponent.Companion::create)
}
