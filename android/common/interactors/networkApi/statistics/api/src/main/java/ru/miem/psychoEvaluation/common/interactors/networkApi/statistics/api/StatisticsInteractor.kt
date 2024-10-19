package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState

interface StatisticsInteractor {
    val commonStatisticsValueScheme: Map<String, Map<String, Int>>?

    suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): StatisticsState

    suspend fun sendAirplaneGameStatistics(
        gsrBreathing: List<Int>,
        gsrGame: List<Int>,
        duration: Int,
        date: String,
    ): SendStatisticsResponseType

    suspend fun sendClocksGameStatistics(
        gsrBreathing: List<Int>,
        gsrGame: List<Int>,
        duration: Int,
        level: Int,
        date: String,
        score: Int,
    ): SendStatisticsResponseType
}
