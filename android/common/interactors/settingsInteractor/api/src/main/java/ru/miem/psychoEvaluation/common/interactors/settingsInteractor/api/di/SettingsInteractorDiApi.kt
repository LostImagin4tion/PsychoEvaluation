package ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di

import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.SettingsInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface SettingsInteractorDiApi : DiApi {
    val settingsInteractor: SettingsInteractor
}