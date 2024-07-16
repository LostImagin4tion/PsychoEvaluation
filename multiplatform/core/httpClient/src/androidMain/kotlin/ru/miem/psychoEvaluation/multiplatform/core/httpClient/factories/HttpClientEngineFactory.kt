package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient

actual fun HttpClientEngineFactory.Creator.newInstance(): HttpClientEngineFactory = createInstance()

fun HttpClientEngineFactory.Creator.createInstance(
    okHttpClient: OkHttpClient = OkHttpClient(),
) : HttpClientEngineFactory = HttpClientEngineFactory {
    OkHttp.create {
        preconfigured = okHttpClient
    }
}

