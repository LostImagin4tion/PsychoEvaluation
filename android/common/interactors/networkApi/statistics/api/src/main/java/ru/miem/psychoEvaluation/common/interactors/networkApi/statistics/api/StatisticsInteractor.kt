package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState

interface StatisticsInteractor {
    val commonStatisticsAirplane: Map<String, Int>?
    val commonStatisticsClock: Map<String, Int>?

    suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): StatisticsState

    suspend fun sendAirplaneGameStatistics(
        data: SendAirplaneGameStatisticsData,
    ): SendStatisticsResponseType

    suspend fun sendClocksGameStatistics(
        data: SendClocksGameStatisticsData
    ): SendStatisticsResponseType

    suspend fun detailedStatistics(
        xDate: String
    ): DetailedStatisticsState

    suspend fun detailedAirplaneStatistics(
        xLevel: String
    ): DetailedAirplaneStatisticsState

    suspend fun detailedClockStatistics(
        xLevel: String
    ): DetailedClockStatisticsState
}
