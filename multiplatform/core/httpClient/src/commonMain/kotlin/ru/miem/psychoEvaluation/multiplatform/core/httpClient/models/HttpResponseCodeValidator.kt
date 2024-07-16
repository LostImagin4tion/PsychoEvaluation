package ru.miem.psychoEvaluation.multiplatform.core.httpClient.models

import io.ktor.http.HttpStatusCode

enum class NetworkResponseType {
    Success,
    Error,
    Unknown
}

typealias HttpResponseCodeValidator = (HttpStatusCode) -> NetworkResponseType

@Suppress("MagicNumber")
fun generalHttpResponseCodeValidator(code: HttpStatusCode): NetworkResponseType {
    return when (code.value) {
        in 200..299 -> NetworkResponseType.Success
        in 400..599 -> NetworkResponseType.Error
        else -> NetworkResponseType.Unknown
    }
}
