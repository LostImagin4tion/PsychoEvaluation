package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual object HttpClientEngineFactory {
    actual fun create(): HttpClientEngine = Darwin.create {
        // TODO ADD CONFIG?
    }
}
