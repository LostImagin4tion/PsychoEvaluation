package ru.miem.psychoEvaluation.feature.registration.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.NothingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.Result
import ru.miem.psychoEvaluation.common.designSystem.utils.ResultNames
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.di.RegistrationInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.model.RegistrationState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import timber.log.Timber

class RegistrationViewModel : ViewModel() {

    private val registrationInteractor by diApi(RegistrationInteractorDiApi::registrationInteractor)

    private val _registrationState = MutableStateFlow<Result<Unit>>(NothingResult())

    val registrationState: StateFlow<Result<Unit>> = _registrationState

    fun registration(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _registrationState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            registrationInteractor.registration(email, password)
                .run {
                    val uiState = this.toResult<Unit>()
                    Timber.tag(TAG).d("Got new UI state $uiState")
                    _registrationState.emit(uiState)
                }
        }
    }

    private fun <T> RegistrationState.toResult(): Result<T> = when (this.state) {
        RegistrationResponseType.Registered -> SuccessResult()
        RegistrationResponseType.AlreadyRegistered -> ErrorResult(R.string.already_registered_alert)
        else -> ErrorResult(R.string.registration_invalid_data_alert)
    }

    private companion object {
        val TAG: String = RegistrationViewModel::class.java.simpleName
    }
}
