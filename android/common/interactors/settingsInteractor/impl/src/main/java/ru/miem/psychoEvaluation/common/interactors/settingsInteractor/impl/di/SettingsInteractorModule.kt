package ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.SettingsInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl.SettingsInteractorImpl

@Module
interface SettingsInteractorModule {

    @Binds
    fun bindSettingsInteractor(impl: SettingsInteractorImpl): SettingsInteractor
}
