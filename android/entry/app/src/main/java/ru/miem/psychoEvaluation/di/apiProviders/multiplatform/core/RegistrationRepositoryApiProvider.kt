package ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.multiplatform.core.di.RegistrationRepositoryComponent
import ru.miem.psychoEvaluation.multiplatform.core.di.RegistrationRepositoryDiApi

@Module
class RegistrationRepositoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(RegistrationRepositoryDiApi::class)
    fun provideRegistrationRepositoryApiProvider() =
        ApiProvider(RegistrationRepositoryComponent::create)
}
