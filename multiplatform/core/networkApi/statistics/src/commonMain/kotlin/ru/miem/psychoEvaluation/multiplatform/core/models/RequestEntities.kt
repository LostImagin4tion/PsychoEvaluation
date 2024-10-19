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
    @SerialName("duration") val gameDuration: Int,
    @SerialName("level") val gameLevel: Int, // always 1
    @SerialName("date") val date: String,
)

@Serializable
data class SendClocksGameStatisticsRequest(
    @SerialName("gsr_breathing") val gsrBreathing: List<Int>,
    @SerialName("gsr_game") val gsrGame: List<Int>,
    @SerialName("duration") val gameDuration: Int,
    @SerialName("level") val gameLevel: Int, // 1 indicates stopwatch, 2 - clocks
    @SerialName("date") val date: String,
    @SerialName("score") val gameScore: Int,
)

