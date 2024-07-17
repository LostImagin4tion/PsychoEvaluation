package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di

import android.app.Application
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.HttpClientEngineFactory
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.HttpClientFactory
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.createInstance
import java.io.File

@Module
interface HttpClientModule {

    companion object {

        @Provides
        @Suppress("MagicNumber")
        fun provideOkHttpClient(application: Application): OkHttpClient {
            val cacheDir = File(application.cacheDir, "okhttp")
            val cache = Cache(cacheDir, (10 * 1024 * 1024).toLong())

            return OkHttpClient.Builder()
                .cache(cache)
                .build()
        }

        @Provides
        fun provideHttpClientEngineFactory(
            okHttpClient: Lazy<OkHttpClient>
        ): HttpClientEngineFactory =
            HttpClientEngineFactory.Creator.createInstance(okHttpClient.get())

        @Provides
        fun provideHttpClientFactory(
            httpClientEngineFactory: HttpClientEngineFactory
        ): HttpClientFactory = HttpClientFactory(httpClientEngineFactory)
    }
}
