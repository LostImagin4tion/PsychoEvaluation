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

data class DetailedForLevelsAirplaneStatisticsState(
    val state: StatisticsResponseType,
    val meanGsrBreathing: Float? = null,
    val meanGsrGame: Float? = null,
    val meanDuration: Float? = null,
    val gamesAmount: Float? = null
)

data class DetailedForLevelsClockStatisticsState(
    val state: StatisticsResponseType,
    val meanGsrBreathing: Float? = null,
    val meanGsrGame: Float? = null,
    val meanDuration: Float? = null,
    val meanScore: Float? = null,
    val gamesAmount: Float? = null
)

data class DetailedAirplaneStatisticsState(
    val state: StatisticsResponseType,
    val duration: Int? = null,
    val meanGsrBreathing: Float? = null,
    val meanGsrGame: Float? = null,
    val gsr: List<Int>? = null,
    val gameId: Int? = null,
    val level: Int? = null,
    val date: String? = null,
    val gsrUpperLimit: Float? = null,
    val gsrLowerLimit: Float? = null,
    val timePercentInLimits: Int? = null,
    val timeInLimits: Int? = null,
    val timeAboveUpperLimit: Int? = null,
    val timeUnderLowerLimit: Int? = null,
    val amountOfCrossingLimits: Int? = null
)

data class DetailedClockStatisticsState(
    val state: StatisticsResponseType,
    val duration: Int? = null,
    val meanGsrGame: Float? = null,
    val gsr: List<Int>? = null,
    val gameId: Int? = null,
    val level: Int? = null,
    val date: String? = null,
    val score: Int? = null,
    val concentrationRate: String? = null,
    val vigilanceRate: String? = null,
    val meanReactionSpeed: Float? = null
)

enum class StatisticsResponseType {
    StatisticAvailable,
    Error,
    LoadungResult
}
