package ru.miem.psychoEvaluation.multiplatform.core.httpClient

import io.ktor.client.HttpClient

object MultiplatformNetwork {
    fun configureHttpClient(
        client: HttpClient
    ): HttpClient {
        return client.config {
            // TODO ADD CONFIG
        }
    }
}
