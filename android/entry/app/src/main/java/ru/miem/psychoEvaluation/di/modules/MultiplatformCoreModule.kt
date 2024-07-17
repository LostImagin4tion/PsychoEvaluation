package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di.HttpClientModule

@Module(
    includes = [
        HttpClientModule::class,
    ]
)
interface MultiplatformCoreModule
