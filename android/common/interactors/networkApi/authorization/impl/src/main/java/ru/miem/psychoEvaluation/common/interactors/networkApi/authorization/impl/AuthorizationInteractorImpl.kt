package ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.impl

import kotlinx.coroutines.flow.first
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.AuthorizationInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.model.AuthorizationResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.model.AuthorizationState
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.AuthorizationRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.AuthorizationRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshAccessTokenRequest
import timber.log.Timber
import javax.inject.Inject

class AuthorizationInteractorImpl @Inject constructor() : AuthorizationInteractor {

    private val dataStore by diApi(DataStorageDiApi::dataStorage)
    private val authorizationRepository by diApi(AuthorizationRepositoryDiApi::authorizationRepository)

    override var apiAccessToken: String? = null
        private set

    override suspend fun authorization(
        email: String?,
        password: String?,
    ): AuthorizationState {
        val authorizationWithRefreshTokenResponse = tryAuthorizationWithRefreshToken()

        return when {
            email == null || password == null ||
                authorizationWithRefreshTokenResponse == AuthorizationResponseType.Authorized
            -> AuthorizationState(authorizationWithRefreshTokenResponse)

            else -> {
                val requestEntity = AuthorizationRequest(email, password)

                authorizationRepository.authorization(requestEntity)
                    .also {
                        Timber.tag(TAG).d("Got authorization response $it")
                    }
                    ?.run {
                        dataStore.set(DataStorageKeys.refreshToken, refreshToken)
                        apiAccessToken = accessToken
                        AuthorizationState(AuthorizationResponseType.Authorized)
                    }
                    ?: AuthorizationState(AuthorizationResponseType.WrongCredentials)
            }
        }
    }

    private suspend fun tryAuthorizationWithRefreshToken(): AuthorizationResponseType {
        val refreshToken = dataStore[DataStorageKeys.refreshToken]
            .first()
            .takeIf { it.isNotBlank() }
            ?: return AuthorizationResponseType.NoRefreshToken

        val requestEntity = RefreshAccessTokenRequest(refreshToken)

        return authorizationRepository.refreshAccessToken(requestEntity)
            .also {
                Timber.tag(TAG).d("Got refresh token response $it")
            }
            ?.run {
                dataStore.set(DataStorageKeys.refreshToken, refreshToken)
                apiAccessToken = accessToken
                AuthorizationResponseType.Authorized
            }
            ?: AuthorizationResponseType.RefreshTokenExpired
    }

    private companion object {
        val TAG: String = AuthorizationInteractorImpl::class.java.simpleName
    }
}
