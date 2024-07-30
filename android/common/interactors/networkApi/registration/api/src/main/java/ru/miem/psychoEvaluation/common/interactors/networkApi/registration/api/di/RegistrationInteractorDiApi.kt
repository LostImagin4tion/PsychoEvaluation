package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.di

import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.RegistrationInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface RegistrationInteractorDiApi : DiApi {
    val registrationInteractor: RegistrationInteractor
}
