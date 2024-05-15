package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories

import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.SafeHttpClient
import kotlin.concurrent.Volatile

class SafeHttpClientFactory(
    private val baseClientProvider: () -> HttpClient
) {
    private val mutex = Mutex()

    @Volatile
    private var safeHttpClient: SafeHttpClient? = null

    suspend fun getSafeHttpClient(): SafeHttpClient {
        return safeHttpClient ?: mutex.withLock {
            safeHttpClient ?: withContext(Dispatchers.Default) {
                SafeHttpClient(baseClientProvider()).also { safeHttpClient = it }
            }
        }
    }
}
