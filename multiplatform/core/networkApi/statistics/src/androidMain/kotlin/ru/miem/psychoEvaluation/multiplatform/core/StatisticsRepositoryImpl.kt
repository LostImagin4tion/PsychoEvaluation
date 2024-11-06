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
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedAirplaneStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedAirplaneStatisticsResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedClockStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedClockStatisticsResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsAirplaneStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsAirplaneStatisticsResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsClockStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsClockStatisticsResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedStatisticsResponse
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
        val url = URLBuilder(StatisticsBaseUrlV1)
            .appendPathSegments(CommonStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<StatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization-token", request.accessToken)
                    append("X-Start-Date", request.startDate)
                    append("X-End-Date", request.endDate)
                }
            }
            .successOrNull()
    }

    override suspend fun sendAirplaneStatistics(
        request: SendAirplaneGameStatisticsRequest,
        token: String,
    ): Boolean {
        val url = URLBuilder(StatisticsBaseUrlV2)
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
        val url = URLBuilder(StatisticsBaseUrlV2)
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

    override suspend fun detailedStatistics(
        request: DetailedStatisticsRequest
    ): DetailedStatisticsResponse? {
        val url = URLBuilder(StatisticsBaseUrl)
            .appendPathSegments(DetailedStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<DetailedStatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization-token", request.accessToken)
                    append("X-Date", request.xDate)
                }
            }
            .successOrNull()
    }

    override suspend fun detailedForLevelsAirplaneStatistics(
        request: DetailedForLevelsAirplaneStatisticsRequest
    ): DetailedForLevelsAirplaneStatisticsResponse? {
        val url = URLBuilder(StatisticsBaseUrl)
            .appendPathSegments(DetailedForLevelsAirplaneStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<DetailedForLevelsAirplaneStatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization-token", request.accessToken)
                    append("X-Level", request.xLevel)
                }
            }
            .successOrNull()
    }

    override suspend fun detailedForLevelsClockStatistics(
        request: DetailedForLevelsClockStatisticsRequest
    ): DetailedForLevelsClockStatisticsResponse? {
        val url = URLBuilder(StatisticsBaseUrl)
            .appendPathSegments(DetailedForLevelsClockStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<DetailedForLevelsClockStatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization-token", request.accessToken)
                    append("X-Level", request.xLevel)
                }
            }
            .successOrNull()
    }

    override suspend fun detailedAirplaneStatistics(
        request: DetailedAirplaneStatisticsRequest
    ): DetailedAirplaneStatisticsResponse? {
        val url = URLBuilder(StatisticsV2BaseUrl)
            .appendPathSegments(DetailedAirplaneStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<DetailedAirplaneStatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization-token", request.accessToken)
                    append("Game-Id", request.gameId)
                }
            }
            .successOrNull()
    }

    override suspend fun detailedClockStatistics(
        request: DetailedClockStatisticsRequest
    ): DetailedClockStatisticsResponse? {
        val url = URLBuilder(StatisticsV2BaseUrl)
            .appendPathSegments(DetailedClockStatisticsEndpoint)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<DetailedClockStatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization-token", request.accessToken)
                    append("Game-Id", request.gameId)
                }
            }
            .successOrNull()
    }

    private companion object {
        const val StatisticsBaseUrlV1 = "http://gsr.miem2.vmnet.top/api/v1"
        const val StatisticsBaseUrlV2 = "http://gsr.miem2.vmnet.top/api/v2"

        const val StatisticsBaseUrl = "http://gsr.miem2.vmnet.top/api/v1"
        const val StatisticsV2BaseUrl = "http://gsr.miem2.vmnet.top/api/v2"
        const val CommonStatisticsEndpoint = "games/dates"
        const val SendAirplaneStatisticsEndpoint = "games/airplane"
        const val SendClocksStatisticsEndpoint = "games/clock"
        const val DetailedStatisticsEndpoint = "games/dates/details"
        const val DetailedForLevelsAirplaneStatisticsEndpoint = "games/airplane/statistics"
        const val DetailedForLevelsClockStatisticsEndpoint = "games/clock/statistics"
        const val DetailedClockStatisticsEndpoint = "games/clock"
        const val DetailedAirplaneStatisticsEndpoint = "games/airplane"

        const val AuthorizationTokenHeader = "Authorization-token"
    }
}
