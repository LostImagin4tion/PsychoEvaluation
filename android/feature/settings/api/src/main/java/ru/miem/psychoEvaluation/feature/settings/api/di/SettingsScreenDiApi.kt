package ru.miem.psychoEvaluation.feature.settings.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.settings.api.SettingsScreen

interface SettingsScreenDiApi : DiApi {
    val settingsScreen: SettingsScreen
}
