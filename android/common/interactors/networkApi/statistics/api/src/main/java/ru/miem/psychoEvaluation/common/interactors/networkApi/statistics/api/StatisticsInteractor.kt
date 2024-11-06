package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.CommonStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClocksStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType

interface StatisticsInteractor {
    suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): CommonStatisticsState

    suspend fun sendAirplaneGameStatistics(
        data: SendAirplaneGameStatisticsData,
    ): SendStatisticsResponseType

    suspend fun sendClocksGameStatistics(
        data: SendClocksGameStatisticsData
    ): SendStatisticsResponseType

    suspend fun detailedStatistics(
        xDate: String
    ): DetailedStatisticsState

    suspend fun detailedForLevelsAirplaneStatistics(
        xLevel: String
    ): DetailedForLevelsAirplaneStatisticsState

    suspend fun detailedForLevelsClockStatistics(
        xLevel: String
    ): DetailedForLevelsClockStatisticsState

    suspend fun detailedAirplaneStatistics(
        gameId: String
    ): DetailedAirplaneStatisticsState

    suspend fun detailedClockStatistics(
        gameId: String
    ): DetailedClocksStatisticsState
}
