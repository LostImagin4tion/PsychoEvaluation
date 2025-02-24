package ru.miem.psychoEvaluation.di.apiProviders.android.common

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.di.RegistrationInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl.di.RegistrationInteractorComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class RegistrationInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(RegistrationInteractorDiApi::class)
    fun provideRegistrationInteractorApiProvider() =
        ApiProvider(RegistrationInteractorComponent.Companion::create)
}
