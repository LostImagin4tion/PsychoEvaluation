package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.HttpClientFactory

interface HttpClientFactoryDiApi : DiApi {
    val httpClientFactory: HttpClientFactory
}
