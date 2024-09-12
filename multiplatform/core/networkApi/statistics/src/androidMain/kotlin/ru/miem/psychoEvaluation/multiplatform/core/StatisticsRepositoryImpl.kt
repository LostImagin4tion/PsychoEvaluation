package ru.miem.psychoEvaluation.multiplatform.core

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
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsResponse
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor() : StatisticsRepository {

    private val baseHttpClient by diApi(HttpClientFactoryDiApi::httpClientFactory)

    private val safeHttpClientFactory = SafeHttpClientFactory { baseHttpClient.create() }

    override suspend fun common_statistics(
        request: StatisticsRequest
    ): StatisticsResponse? {
        val url = URLBuilder(STATISTICS_BASE_URL)
            .appendPathSegments(COMMON_STATISTICS_ENDPOINT)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<StatisticsResponse, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .successOrNull()
    }

    private companion object {
        const val STATISTICS_BASE_URL = "http://gsr.miem2.vmnet.top/api/v1"
        const val COMMON_STATISTICS_ENDPOINT = "games/dates"
    }
}
