package ru.miem.psychoEvaluation.di.apiProviders.multiplatform

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di.HttpClientFactoryComponent
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di.HttpClientFactoryDiApi

@Module
class HttpClientFactoryApiProvider {

    @Provides
    @IntoMap
    @ApiKey(HttpClientFactoryDiApi::class)
    fun provideHttpClientFactoryApiProvider(
        application: Application,
    ) = ApiProvider {
        HttpClientFactoryComponent.create(application)
    }
}