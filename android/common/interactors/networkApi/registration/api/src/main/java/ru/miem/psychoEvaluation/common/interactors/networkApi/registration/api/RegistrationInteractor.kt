package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationState

interface RegistrationInteractor {
    val apiAccessToken: String?

    suspend fun registration(
        email: String? = null,
        password: String? = null
    ): RegistrationState
}
