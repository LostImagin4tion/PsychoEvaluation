package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.engine.HttpClientEngine

fun interface HttpClientEngineFactory {
    fun create(): HttpClientEngine

    companion object Creator
}

expect fun HttpClientEngineFactory.Creator.newInstance(): HttpClientEngineFactory
