package ru.miem.psychoEvaluation.multiplatform.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponse(
    @SerialName("airplane") val airplane: Map<String, Int>?,
    @SerialName("clock") val clock: Map<String, Int>?
)

@Serializable
data class SendStatisticsResponse(
    @SerialName("id") val id: Int,
)

@Serializable
data class DetailedStatisticsResponse(
    @SerialName("airplane") val airplane: List<AirplaneData>,
    @SerialName("clock") val clock: List<ClockData>
)

@Serializable
data class AirplaneData(
    @SerialName("id") val id: Int,
    @SerialName("date") val date: String,
    @SerialName("duration") val duration: String,
    @SerialName("level") val level: Int
)

@Serializable
data class ClockData(
    @SerialName("id") val id: Int,
    @SerialName("date") val date: String,
    @SerialName("duration") val duration: String,
    @SerialName("level") val level: Int
)

@Serializable
data class DetailedForLevelsAirplaneStatisticsResponse(
    @SerialName("mean_gsr_breathing") val meanGsrBreathing: Float,
    @SerialName("mean_gsr_game") val meanGsrGame: Float,
    @SerialName("mean_duration") val meanDuration: Float,
    @SerialName("games_amount") val gamesAmount: Float
)

@Serializable
data class DetailedForLevelsClockStatisticsResponse(
    @SerialName("mean_gsr_breathing") val meanGsrBreathing: Float,
    @SerialName("mean_gsr_game") val meanGsrGame: Float,
    @SerialName("mean_duration") val meanDuration: Float,
    @SerialName("mean_score") val meanScore: Float,
    @SerialName("games_amount") val gamesAmount: Float
)

@Serializable
data class DetailedAirplaneStatisticsResponse(
    @SerialName("duration") val duration: Int,
    @SerialName("mean_gsr_game") val meanGsrGame: Float,
    @SerialName("mean_gsr_breathing") val meanGsrBreathing: Float,
    @SerialName("gsr") val gsr: List<Int>,
    @SerialName("game_id") val gameId: Int,
    @SerialName("level") val level: Int,
    @SerialName("date") val date: String,
    @SerialName("gsr_upper_limit") val gsrUpperLimit: Float,
    @SerialName("gsr_lower_limit") val gsrLowerLimit: Float,
    @SerialName("time_percent_in_limits") val timePercentInLimits: Int,
    @SerialName("time_in_limits") val timeInLimits: Int,
    @SerialName("time_above_upper_limit") val timeAboveUpperLimit: Int,
    @SerialName("time_under_lower_limit") val timeUnderLowerLimit: Int,
    @SerialName("amount_of_crossing_limits") val amountOfCrossingLimits: Int
)

@Serializable
data class DetailedClockStatisticsResponse(
    @SerialName("duration") val duration: Int,
    @SerialName("mean_gsr_game") val meanGsrGame: Float,
    @SerialName("mean_gsr_breathing") val meanGsrBreathing: Float,
    @SerialName("gsr") val gsr: List<Int>,
    @SerialName("game_id") val gameId: Int,
    @SerialName("level") val level: Int,
    @SerialName("date") val date: String,
    @SerialName("gsr_upper_limit") val gsrUpperLimit: Float,
    @SerialName("gsr_lower_limit") val gsrLowerLimit: Float,
    @SerialName("time_percent_in_limits") val timePercentInLimits: Int,
    @SerialName("time_in_limits") val timeInLimits: Int,
    @SerialName("time_above_upper_limit") val timeAboveUpperLimit: Int,
    @SerialName("time_under_lower_limit") val timeUnderLowerLimit: Int,
    @SerialName("amount_of_crossing_limits") val amountOfCrossingLimits: Int
)
