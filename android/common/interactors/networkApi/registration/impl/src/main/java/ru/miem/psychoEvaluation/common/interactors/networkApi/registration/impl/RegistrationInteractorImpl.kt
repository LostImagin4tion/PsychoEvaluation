package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl

import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.RegistrationInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationState
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.RegistrationRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationRequest
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
        return when {
            email == null || password == null -> RegistrationState(RegistrationResponseType.WrongCredentials)
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
                    ?: RegistrationState(RegistrationResponseType.AlreadyRegistered)
            }
        }
    }

    private companion object {
        val TAG: String = RegistrationInteractorImpl::class.java.simpleName
    }
}
