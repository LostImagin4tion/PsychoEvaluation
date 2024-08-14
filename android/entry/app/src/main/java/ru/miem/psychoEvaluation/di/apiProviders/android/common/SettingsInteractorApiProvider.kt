package ru.miem.psychoEvaluation.di.apiProviders.android.common

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl.di.SettingsInteractorComponent
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class SettingsInteractorApiProvider {

    @Provides
    @IntoMap
    @ApiKey(SettingsInteractorDiApi::class)
    fun provideSettingsInteractorApiProvider() = ApiProvider(SettingsInteractorComponent.Companion::create)
}
