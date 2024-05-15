package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.MultiplatformNetwork

class HttpClientFactory(
    private val configuration: HttpClientConfig<*>.() -> Unit = {}
) {
    private val httpClient: HttpClient by lazy {
        HttpClient(HttpClientEngineFactory.create())
    }

    fun create(): HttpClient = httpClient
        .config {
            configuration()
        }
        .let(MultiplatformNetwork::configureHttpClient)
}
