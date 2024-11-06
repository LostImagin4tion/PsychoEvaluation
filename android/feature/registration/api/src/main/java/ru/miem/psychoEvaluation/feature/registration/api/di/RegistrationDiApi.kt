package ru.miem.psychoEvaluation.feature.registration.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen

interface RegistrationDiApi : DiApi {
    val registrationScreen: RegistrationScreen
}
