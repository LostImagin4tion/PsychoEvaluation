package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsResponse

interface StatisticsRepository {
    suspend fun commonStatistics(request: StatisticsRequest): StatisticsResponse?
}
