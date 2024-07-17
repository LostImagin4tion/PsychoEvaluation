package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di

import android.app.Application
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.HttpClientEngineFactory
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.HttpClientFactory
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.createInstance
import java.io.File

@Module
interface HttpClientFactoryModule {

    companion object {

        @Provides
        fun provideHttpClientFactory(
            httpClientEngineFactory: HttpClientEngineFactory
        ): HttpClientFactory = HttpClientFactory(httpClientEngineFactory) {
            install(ContentNegotiation) {
                json()
            }
        }

        @Provides
        fun provideHttpClientEngineFactory(
            okHttpClient: Lazy<OkHttpClient>
        ): HttpClientEngineFactory =
            HttpClientEngineFactory.Creator.createInstance(okHttpClient.get())

        @Provides
        @Suppress("MagicNumber")
        fun provideOkHttpClient(application: Application): OkHttpClient {
            val cacheDir = File(application.cacheDir, "okhttp")
            val cache = Cache(cacheDir, (10 * 1024 * 1024).toLong())

            return OkHttpClient.Builder()
                .setupHttpSecurityLevel()
                .addLoggingInterceptor()
                .cache(cache)
                .build()
        }

        private fun OkHttpClient.Builder.setupHttpSecurityLevel(): OkHttpClient.Builder {
            val specs = listOf(
                ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT
            )
            return this.connectionSpecs(specs)
        }

        private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            addNetworkInterceptor(loggingInterceptor)
            return this
        }
    }
}
