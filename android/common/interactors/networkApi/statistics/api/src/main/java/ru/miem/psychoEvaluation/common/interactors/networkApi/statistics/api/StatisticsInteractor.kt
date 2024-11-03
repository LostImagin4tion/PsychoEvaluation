package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState

interface StatisticsInteractor {
    val commonStatisticsValueScheme: Map<String, Map<String, Int>>?

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
}
