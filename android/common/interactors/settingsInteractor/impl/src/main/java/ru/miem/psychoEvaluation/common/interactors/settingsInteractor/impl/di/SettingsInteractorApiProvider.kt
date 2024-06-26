package ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class SettingsInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(SettingsInteractorDiApi::class)
    fun provideSettingsInteractorApiProvider() = ApiProvider(SettingsInteractorComponent::create)
}