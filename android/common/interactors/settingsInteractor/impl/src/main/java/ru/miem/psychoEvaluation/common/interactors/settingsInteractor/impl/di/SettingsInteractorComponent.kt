package ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi

@Component(
    modules = [
        SettingsInteractorModule::class
    ]
)
interface SettingsInteractorComponent : SettingsInteractorDiApi {
    companion object {
        fun create(): SettingsInteractorDiApi = DaggerSettingsInteractorComponent.builder().build()
    }
}
