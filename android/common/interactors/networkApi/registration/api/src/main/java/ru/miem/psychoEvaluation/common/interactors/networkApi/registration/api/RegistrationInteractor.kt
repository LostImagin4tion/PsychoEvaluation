package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationState

interface RegistrationInteractor {

    suspend fun registration(
        email: String,
        password: String
    ): RegistrationState
}
