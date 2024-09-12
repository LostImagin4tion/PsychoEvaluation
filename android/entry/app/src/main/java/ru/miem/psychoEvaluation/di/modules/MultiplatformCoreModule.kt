package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core.AuthorizationRepositoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core.HttpClientFactoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.core.RegistrationRepositoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.multiplatform.StatisticsRepositoryApiProvider

@Module(
    includes = [
        HttpClientFactoryApiProvider::class,
        AuthorizationRepositoryApiProvider::class,
        RegistrationRepositoryApiProvider::class,
        StatisticsRepositoryApiProvider::class
    ]
)
interface MultiplatformCoreModule
