package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model

data class RegistrationState(
    val state: RegistrationResponseType
)

enum class RegistrationResponseType {
    Registered,
    RefreshTokenExpired,
    WrongCredentials
}
