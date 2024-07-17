package ru.miem.psychoEvaluation.multiplatform.core.httpClient

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.models.HttpResponseCodeValidator
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.models.NetworkResponse
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.models.NetworkResponseType
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.models.generalHttpResponseCodeValidator
import ru.miem.psychoEvaluation.multiplatform.core.utils.logger.Logger

class SafeHttpClient internal constructor(baseHttpClient: HttpClient) {

    @PublishedApi
    internal val httpClient = baseHttpClient.config { expectSuccess = false }

    @Suppress("TooGenericExceptionCaught")
    suspend inline fun <reified DataType : Any, ErrorType : Any> requestOnBackground(
        url: String,
        crossinline responseValidator: HttpResponseCodeValidator = ::generalHttpResponseCodeValidator,
        crossinline requestBuilder: HttpRequestBuilder.() -> Unit,
    ): NetworkResponse<DataType, ErrorType> {
        return withContext(Dispatchers.IO) {
            val client = httpClient
            var httpInfo: NetworkResponse.HttpInfo? = null

            try {
                val response = client.request(url, requestBuilder)
                httpInfo = response.httpInfo

                try {
                    when (responseValidator(response.status)) {
                        NetworkResponseType.Success -> NetworkResponse.Success(
                            data = response.getBody(),
                            httpInfo = httpInfo,
                        )
                        NetworkResponseType.Error,
                        NetworkResponseType.Unknown
                        -> NetworkResponse.Error(httpInfo = httpInfo)
                    }
                } catch (throwable: Throwable) {
                    Logger.e(throwable, "Failed retrieving data from response")
                    NetworkResponse.Error(httpInfo)
                }
            } catch (throwable: Throwable) {
                Logger.e(throwable, "Failed http request")
                NetworkResponse.Error()
            }
        }
    }

    @PublishedApi
    internal val HttpResponse.httpInfo: NetworkResponse.HttpInfo
        get() = NetworkResponse.HttpInfo(
            httpCode = status,
            headers = headers,
        )

    @PublishedApi
    internal suspend inline fun <reified T> HttpResponse.getBody(): T {
        return when {
            T::class == Unit::class -> Unit as T
            T::class == String::class -> bodyAsText() as T
            else -> body()
        }
    }
}
