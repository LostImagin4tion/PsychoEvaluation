package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.AuthorizationRepositoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.HttpClientFactoryApiProvider

@Module(
    includes = [
        HttpClientFactoryApiProvider::class,
        AuthorizationRepositoryApiProvider::class,
    ]
)
interface MultiplatformCoreModule
