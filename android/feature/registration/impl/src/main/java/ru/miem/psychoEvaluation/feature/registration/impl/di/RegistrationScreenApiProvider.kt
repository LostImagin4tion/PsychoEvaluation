package ru.miem.psychoEvaluation.feature.registration.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationDiApi

@Module
class RegistrationScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(RegistrationDiApi::class)
    fun providesRegistrationScreenApiProvider() = ApiProvider(RegistrationScreenComponent::create)
}
