package ru.miem.psychoEvaluation.multiplatform.core

import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.SafeHttpClientFactory
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.factories.di.HttpClientFactoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.httpClient.models.successOrNull
import ru.miem.psychoEvaluation.multiplatform.core.models.SendAirplaneGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendClocksGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendStatisticsResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsResponse
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor() : StatisticsRepository {

    private val baseHttpClient by diApi(HttpClientFactoryDiApi::httpClientFactory)

    private val safeHttpClientFactory = SafeHttpClientFactory { baseHttpClient.create() }

    override suspend fun commonStatistics(
        request: StatisticsRequest
    ): StatisticsResponse? {
        val url = URLBuilder(StatisticsBaseUrl)
            .appendPathSegments(CommonStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<StatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .successOrNull()
    }

    override suspend fun sendAirplaneStatistics(
        request: SendAirplaneGameStatisticsRequest,
        token: String,
    ): Boolean {
        val url = URLBuilder(StatisticsBaseUrl)
            .appendPathSegments(SendAirplaneStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<SendStatisticsResponse, Unit>(url) {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(request)

                headers {
                    append(AuthorizationTokenHeader, token)
                }
            }
            .successOrNull()
            .let { it != null }
    }

    override suspend fun sendClocksStatistics(
        request: SendClocksGameStatisticsRequest,
        token: String,
    ): Boolean {
        val url = URLBuilder(StatisticsBaseUrl)
            .appendPathSegments(SendClocksStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<SendStatisticsResponse, Unit>(url) {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(request)

                headers {
                    append(AuthorizationTokenHeader, token)
                }
            }
            .successOrNull()
            .let { it != null }
    }

    private companion object {
        const val StatisticsBaseUrl = "http://gsr.miem2.vmnet.top/api/v1"
        const val CommonStatisticsEndpoint = "games/dates"
        const val SendAirplaneStatisticsEndpoint = "games/airplane"
        const val SendClocksStatisticsEndpoint = "games/clock"

        const val AuthorizationTokenHeader = "Authorization-token"
    }
}
