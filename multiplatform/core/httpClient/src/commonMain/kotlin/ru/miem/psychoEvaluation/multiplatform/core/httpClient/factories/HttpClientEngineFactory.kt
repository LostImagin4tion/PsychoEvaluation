package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.engine.HttpClientEngine

expect object HttpClientEngineFactory {
    fun create(): HttpClientEngine
}
