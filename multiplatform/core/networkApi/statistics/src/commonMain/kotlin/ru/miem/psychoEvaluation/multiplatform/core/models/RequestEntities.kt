package ru.miem.psychoEvaluation.multiplatform.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsRequest(
    @SerialName("access_token") val accessToken: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
)

@Serializable
data class SendAirplaneGameStatisticsRequest(
    @SerialName("gsr_breathing") val gsrBreathing: List<Int>,
    @SerialName("gsr_game") val gsrGame: List<Int>,
    @SerialName("duration") val gameDuration: Long,
    @SerialName("level") val gameLevel: Int, // always 1
    @SerialName("date") val date: String,
    @SerialName("gsr_upper_limit") val gsrUpperLimit: Float,
    @SerialName("gsr_lower_limit") val gsrLowerLimit: Float,
    @SerialName("time_percent_in_limits") val timePercentInLimits: Int,
    @SerialName("time_in_limits") val timeInLimits: Long,
    @SerialName("time_above_upper_limit") val timeAboveUpperLimit: Long,
    @SerialName("time_under_lower_limit") val timeUnderLowerLimit: Long,
    @SerialName("amount_of_crossing_limits") val amountOfCrossingLimits: Int,
)

@Serializable
data class SendClocksGameStatisticsRequest(
    @SerialName("gsr_game") val gsrGame: List<Int>,
    @SerialName("duration") val gameDuration: Long,
    @SerialName("level") val gameLevel: Int, // 1 - stopwatch, 2 - clocks
    @SerialName("date") val date: String,
    @SerialName("score") val gameScore: Int,
    @SerialName("reaction_speed") val reactionSpeed: List<Long>,
)

