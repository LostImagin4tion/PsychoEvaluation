package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.SendAirplaneGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendClocksGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsResponse

interface StatisticsRepository {
    suspend fun commonStatistics(request: StatisticsRequest): StatisticsResponse?

    suspend fun sendAirplaneStatistics(
        request: SendAirplaneGameStatisticsRequest,
        token: String,
    ): Boolean

    suspend fun sendClocksStatistics(
        request: SendClocksGameStatisticsRequest,
        token: String,
    ): Boolean
}
