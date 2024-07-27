package ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        HttpClientFactoryModule::class
    ]
)
interface HttpClientFactoryComponent : HttpClientFactoryDiApi {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): HttpClientFactoryComponent
    }

    companion object {
        fun create(
            application: Application
        ): HttpClientFactoryDiApi = DaggerHttpClientFactoryComponent.builder()
            .application(application)
            .build()
    }
}
