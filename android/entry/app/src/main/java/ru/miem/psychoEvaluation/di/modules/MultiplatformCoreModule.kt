package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.AuthorizationRepositoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.HttpClientFactoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.RegistrationRepositoryApiProvider

@Module(
    includes = [
        HttpClientFactoryApiProvider::class,
        AuthorizationRepositoryApiProvider::class,
        RegistrationRepositoryApiProvider::class
    ]
)
interface MultiplatformCoreModule
