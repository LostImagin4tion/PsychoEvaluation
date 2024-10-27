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
    @SerialName("level") val level: Int
)

@Serializable
data class ClockData(
    @SerialName("id") val id: Int,
    @SerialName("date") val date: String,
    @SerialName("level") val level: Int
)

@Serializable
data class DetailedAirplaneStatisticsResponse(
    @SerialName("mean_gsr_breathing") val meanGsrBreathing: Float,
    @SerialName("mean_gsr_game") val meanGsrGame: Float,
    @SerialName("mean_duration") val meanDuration: Float,
    @SerialName("games_amount") val gamesAmount: Float
)

@Serializable
data class DetailedClockStatisticsResponse(
    @SerialName("mean_gsr_breathing") val meanGsrBreathing: Float,
    @SerialName("mean_gsr_game") val meanGsrGame: Float,
    @SerialName("mean_duration") val meanDuration: Float,
    @SerialName("mean_score") val meanScore: Float,
    @SerialName("games_amount") val gamesAmount: Float
)
