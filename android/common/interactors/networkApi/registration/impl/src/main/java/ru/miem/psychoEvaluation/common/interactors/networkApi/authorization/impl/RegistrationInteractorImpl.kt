package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl

import kotlinx.coroutines.flow.first
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.RegistrationInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationState
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.RegistrationRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshAccessTokenRequest
import timber.log.Timber
import javax.inject.Inject

class RegistrationInteractorImpl @Inject constructor() : RegistrationInteractor {

    private val dataStore by diApi(DataStorageDiApi::dataStorage)
    private val registrationRepository by diApi(RegistrationRepositoryDiApi::registrationRepository)

    override var apiAccessToken: String? = null
        private set

    override suspend fun registration(
        email: String?,
        password: String?,
    ): RegistrationState {
        val authorizedWithRefreshToken = tryAuthorizationWithRefreshToken()

        return when {
            authorizedWithRefreshToken -> RegistrationState((RegistrationResponseType.Registered))
            email == null || password == null -> RegistrationState(RegistrationResponseType.RefreshTokenExpired)
            else -> {
                val requestEntity = RegistrationRequest(email, password)

                registrationRepository.registration(requestEntity)
                    .also {
                        Timber.tag(TAG).d("Got registration response $it")
                    }
                    ?.run {
                        dataStore.set(DataStorageKeys.refreshToken, refreshToken)
                        apiAccessToken = accessToken
                        RegistrationState(RegistrationResponseType.Registered)
                    }
                    ?: RegistrationState(RegistrationResponseType.WrongCredentials)
            }
        }
    }

    private suspend fun tryAuthorizationWithRefreshToken(): Boolean {
        val refreshToken = dataStore[DataStorageKeys.refreshToken]
            .first()
            .takeIf { it.isNotBlank() }
            ?: return false

        val requestEntity = RefreshAccessTokenRequest(refreshToken)

        return registrationRepository.refreshAccessToken(requestEntity)
            .also {
                Timber.tag(TAG).d("Got refresh token response $it")
            }
            ?.run {
                dataStore.set(DataStorageKeys.refreshToken, refreshToken)
                apiAccessToken = accessToken
                true
            }
            ?: false
    }

    private companion object {
        val TAG: String = RegistrationInteractorImpl::class.java.simpleName
    }
}
