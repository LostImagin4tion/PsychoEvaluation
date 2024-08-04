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
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshAccessTokenRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshAccessTokenRequest1
import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationResponse
import ru.miem.psychoEvaluation.multiplatform.core.models.RefreshTokenResponse1
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor() : RegistrationRepository {

    private val baseHttpClient by diApi(HttpClientFactoryDiApi::httpClientFactory)

    private val safeHttpClientFactory = SafeHttpClientFactory { baseHttpClient.create() }

    override suspend fun registration(
        request: RegistrationRequest
    ): RegistrationResponse? {
        val url = URLBuilder(REGISTRATION_BASE_URL)
            .appendPathSegments(REGISTRATION_ENDPOINT)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<RegistrationResponse, Unit>(url) {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .successOrNull()
    }

    override suspend fun refreshAccessToken(request: RefreshAccessTokenRequest1): RefreshTokenResponse1? {
        val url = URLBuilder(REGISTRATION_BASE_URL)
            .appendPathSegments(REFRESH_TOKEN_ENDPOINT)
            .buildString()

        return safeHttpClientFactory.get()
            .requestOnBackground<RefreshTokenResponse1, Unit>(url) {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)

                headers {
                    append(AUTHORIZATION_TOKEN_HEADER, request.refreshToken)
                }
            }
            .successOrNull()
    }

    private companion object {
        const val REGISTRATION_BASE_URL = "http://gsr.miem2.vmnet.top/"
        const val REGISTRATION_ENDPOINT = "users"
        const val REFRESH_TOKEN_ENDPOINT = "users/refresh"

        const val AUTHORIZATION_TOKEN_HEADER = "Authorization-token"
    }
}
