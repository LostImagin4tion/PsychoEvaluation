package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.engine.darwin.Darwin

actual fun HttpClientEngineFactory.Creator.newInstance(): HttpClientEngineFactory = newInstance()

fun HttpClientEngineFactory.Creator.createInstance(): HttpClientEngineFactory = HttpClientEngineFactory {
    Darwin.create {
        // TODO ADD CONFIGURATION?
    }
}
