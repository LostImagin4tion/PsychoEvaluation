package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual object HttpClientEngineFactory {
    actual fun create(): HttpClientEngine = OkHttp.create {
        // TODO ADD CONFIG?
    }
}
