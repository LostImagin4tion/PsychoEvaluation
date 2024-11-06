package ru.miem.psychoEvaluation.multiplatform.core.httpClient.models

import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import ru.miem.psychoEvaluation.multiplatform.core.utils.logger.Logger

sealed class NetworkResponse<out DataType : Any, out ErrorType : Any> {

    data class Success<DataType : Any>(
        val data: DataType,
        val httpInfo: HttpInfo,
    ) : NetworkResponse<DataType, Nothing>()

    data class Error<out ErrorType : Any>(
        val httpInfo: HttpInfo? = null
    ) : NetworkResponse<Nothing, ErrorType>() {
        // TODO design errors hierarchy?
    }

    data class HttpInfo(
        val httpCode: HttpStatusCode,
        val headers: Headers,
    )
}

fun NetworkResponse<*, *>.isSuccess(): Boolean = this is NetworkResponse.Success

fun <DataType : Any> NetworkResponse<DataType, *>.successOrNull(): DataType? = when (this) {
    is NetworkResponse.Success -> this.data
    is NetworkResponse.Error -> {
        Logger.e("Got network error while performing request: $httpInfo")
        null
    }
}
