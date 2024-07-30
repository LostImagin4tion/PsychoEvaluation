package ru.miem.psychoEvaluation.feature.authorization.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.FullScreenLoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.NothingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.Result
import ru.miem.psychoEvaluation.common.designSystem.utils.ResultNames
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.di.AuthorizationInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.model.AuthorizationResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.model.AuthorizationState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.registration.impl.R
import timber.log.Timber

class AuthorizationViewModel : ViewModel() {

    private val authorizationInteractor by diApi(AuthorizationInteractorDiApi::authorizationInteractor)

    private val _authorizationState = MutableStateFlow<Result<Unit>>(NothingResult())

    val authorizationState: StateFlow<Result<Unit>> = _authorizationState

    fun tryAuthorizationWithRefreshToken() {
        viewModelScope.launch {
            _authorizationState.emit(FullScreenLoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.fullScreenLoading}")

            authorizationInteractor.authorization()
                .run {
                    val result = this.toResult<Unit>()
                    Timber.tag(TAG).d("Got new UI state $result")
                    _authorizationState.emit(result)
                }
        }
    }

    fun authorization(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _authorizationState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            authorizationInteractor.authorization(email, password)
                .run {
                    val uiState = this.toResult<Unit>()
                    Timber.tag(TAG).d("Got new UI state $uiState")
                    _authorizationState.emit(uiState)
                }
        }
    }

    private fun <T>AuthorizationState.toResult(): Result<T> = when (this.state) {
        AuthorizationResponseType.Authorized -> SuccessResult()
        AuthorizationResponseType.NoRefreshToken -> ErrorResult()
        AuthorizationResponseType.RefreshTokenExpired -> ErrorResult(R.string.session_expired_alert)
        AuthorizationResponseType.WrongCredentials -> ErrorResult(R.string.wrong_credentials_alert)
    }

    private companion object {
        val TAG: String = AuthorizationViewModel::class.java.simpleName
    }
}
