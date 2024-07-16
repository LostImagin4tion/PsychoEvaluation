package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

class HttpClientFactory(
    private val httpClientEngineFactory: HttpClientEngineFactory,
    private val configuration: HttpClientConfig<*>.() -> Unit = {}
) {
    private val httpClient: HttpClient by lazy {
        HttpClient(httpClientEngineFactory.create())
    }

    fun create(): HttpClient = httpClient
        .config {
            configuration()
        }
}
