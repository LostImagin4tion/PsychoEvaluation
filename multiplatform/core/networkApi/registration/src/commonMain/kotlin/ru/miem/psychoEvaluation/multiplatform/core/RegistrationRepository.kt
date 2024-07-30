package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshAccessTokenRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshTokenResponse

interface RegistrationRepository {
    suspend fun registration(request: RegistrationRequest): RegistrationResponse?
    suspend fun refreshAccessToken(request: RefreshAccessTokenRequest): RefreshTokenResponse?
}
