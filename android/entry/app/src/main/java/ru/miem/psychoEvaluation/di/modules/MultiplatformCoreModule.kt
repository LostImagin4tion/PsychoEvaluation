package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core.AuthorizationRepositoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core.HttpClientFactoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core.RegistrationRepositoryApiProvider

@Module(
    includes = [
        HttpClientFactoryApiProvider::class,
        AuthorizationRepositoryApiProvider::class,
        RegistrationRepositoryApiProvider::class
    ]
)
interface MultiplatformCoreModule
