package ru.miem.psychoEvaluation.common.designSystem.utils

import androidx.annotation.StringRes

sealed class Result<T>

class NothingResult<T> : Result<T>()

class LoadingResult<T> : Result<T>()

class FullScreenLoadingResult<T> : Result<T>()

data class SuccessResult<T>(
    val data: T? = null,
) : Result<T>()

data class ErrorResult<T>(
    @StringRes val message: Int? = null,
) : Result<T>()

object ResultNames {
    val nothing: String = ErrorResult::class.java.simpleName
    val loading: String = LoadingResult::class.java.simpleName
    val fullScreenLoading: String = FullScreenLoadingResult::class.java.simpleName
    val success: String = SuccessResult::class.java.simpleName
    val error: String = ErrorResult::class.java.simpleName
}
