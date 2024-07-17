package ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.model

data class AuthorizationState(
    val state: AuthorizationResponseType
)

enum class AuthorizationResponseType {
    Authorized,
    RefreshTokenExpired,
    WrongCredentials
}
