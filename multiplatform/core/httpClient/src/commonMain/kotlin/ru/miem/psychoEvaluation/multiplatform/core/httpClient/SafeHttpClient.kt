package ru.miem.psychoEvaluation.multiplatform.core.httpClient

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SafeHttpClient internal constructor(baseHttpClient: HttpClient) {

    private val httpClient = baseHttpClient.config { expectSuccess = false }

    suspend fun requestOnBackground(
        url: String,
        // responseValidator: ????, TODO DESIGN THIS THING
        requestBuilder: HttpRequestBuilder.() -> Unit,
    ) /* : NetworkResponse */ {
        return withContext(Dispatchers.IO) {
        }
    }

    fun close() = httpClient.close()
}
