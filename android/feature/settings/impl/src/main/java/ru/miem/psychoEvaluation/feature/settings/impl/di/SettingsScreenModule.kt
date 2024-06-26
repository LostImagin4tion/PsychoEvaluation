package ru.miem.psychoEvaluation.feature.settings.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.settings.api.SettingsScreen
import ru.miem.psychoEvaluation.feature.settings.impl.SettingsScreenImpl

@Module
interface SettingsScreenModule {

    @Binds
    fun bindSettingsScreen(impl: SettingsScreenImpl): SettingsScreen
}
