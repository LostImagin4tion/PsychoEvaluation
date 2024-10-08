package ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.model.AuthorizationState

interface AuthorizationInteractor {

    suspend fun authorization(
        email: String? = null,
        password: String? = null
    ): AuthorizationState
}
