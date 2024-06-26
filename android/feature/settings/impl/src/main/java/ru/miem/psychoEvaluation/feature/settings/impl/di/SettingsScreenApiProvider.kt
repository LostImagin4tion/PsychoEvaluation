package ru.miem.psychoEvaluation.feature.settings.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.settings.api.di.SettingsScreenDiApi

@Module
class SettingsScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(SettingsScreenDiApi::class)
    fun provideSettingsScreenApiProvider() = ApiProvider(SettingsScreenComponent::create)
}
