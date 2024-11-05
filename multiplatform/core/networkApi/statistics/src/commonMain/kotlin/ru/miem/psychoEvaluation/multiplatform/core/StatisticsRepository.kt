package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.AirplaneData
import ru.miem.psychoEvaluation.multiplatform.core.models.ClockData
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
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsResponse

interface StatisticsRepository {
    suspend fun commonStatistics(request: StatisticsRequest): StatisticsResponse?

    suspend fun detailedStatistics(request: DetailedStatisticsRequest): DetailedStatisticsResponse?

    suspend fun detailedForLevelsAirplaneStatistics(
        request: DetailedForLevelsAirplaneStatisticsRequest
    ): DetailedForLevelsAirplaneStatisticsResponse?

    suspend fun detailedForLevelsClockStatistics(request: DetailedForLevelsClockStatisticsRequest): DetailedForLevelsClockStatisticsResponse?

    suspend fun detailedAirplaneStatistics(
        request: DetailedAirplaneStatisticsRequest
    ): DetailedAirplaneStatisticsResponse?

    suspend fun detailedClockStatistics(request: DetailedClockStatisticsRequest): DetailedClockStatisticsResponse?

    suspend fun sendAirplaneStatistics(
        request: SendAirplaneGameStatisticsRequest,
        token: String,
    ): Boolean

    suspend fun sendClocksStatistics(
        request: SendClocksGameStatisticsRequest,
        token: String,
    ): Boolean

}
