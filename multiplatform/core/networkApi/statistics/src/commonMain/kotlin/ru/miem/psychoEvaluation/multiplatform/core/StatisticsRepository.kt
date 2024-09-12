package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsResponse

interface StatisticsRepository {
    suspend fun common_statistics(request: StatisticsRequest): StatisticsResponse?
}
