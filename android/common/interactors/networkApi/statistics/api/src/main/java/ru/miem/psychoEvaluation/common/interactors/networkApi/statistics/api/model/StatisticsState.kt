package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

import ru.miem.psychoEvaluation.multiplatform.core.models.AirplaneData
import ru.miem.psychoEvaluation.multiplatform.core.models.ClockData

data class StatisticsState(
    val state: StatisticsResponseType,
    val airplaneData: Map<String, Int>? = null,
    val clockData: Map<String, Int>? = null
)

data class DetailedStatisticsState(
    val state: StatisticsResponseType,
    val detailedAirplaneData: List<AirplaneData>? = null,
    val detailedClockData: List<ClockData>? = null
)

data class DetailedAirplaneStatisticsState(
    val state: StatisticsResponseType,
    val meanGsrBreathing: Float? = null,
    val meanGsrGame: Float? = null,
    val meanDuration: Float? = null,
    val gamesAmount: Float? = null
)

data class DetailedClockStatisticsState(
    val state: StatisticsResponseType,
    val meanGsrBreathing: Float? = null,
    val meanGsrGame: Float? = null,
    val meanDuration: Float? = null,
    val meanScore: Float? = null,
    val gamesAmount: Float? = null
)

enum class StatisticsResponseType {
    StatisticAvailable,
    Error
}
