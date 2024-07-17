package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.AuthorizationRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.AuthorizationResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshAccessTokenRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshTokenResponse

interface AuthorizationRepository {
    suspend fun authorization(request: AuthorizationRequest): AuthorizationResponse?
    suspend fun refreshAccessToken(request: RefreshAccessTokenRequest): RefreshTokenResponse?
}
