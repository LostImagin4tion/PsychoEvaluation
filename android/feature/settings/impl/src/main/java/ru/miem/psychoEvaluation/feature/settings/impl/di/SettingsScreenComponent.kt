package ru.miem.psychoEvaluation.feature.settings.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.settings.api.di.SettingsScreenDiApi

@Component(
    modules = [
        SettingsScreenModule::class,
    ]
)
interface SettingsScreenComponent : SettingsScreenDiApi {
    companion object {
        fun create(): SettingsScreenDiApi = DaggerSettingsScreenComponent.builder().build()
    }
}
