package ru.miem.psychoEvaluation.feature.registration.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen

interface RegistrationApi : Api {
    val registrationScreen: RegistrationScreen
}
