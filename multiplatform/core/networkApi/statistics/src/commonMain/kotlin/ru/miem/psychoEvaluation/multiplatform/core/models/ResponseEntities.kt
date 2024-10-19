package ru.miem.psychoEvaluation.multiplatform.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponse(
    @SerialName("valueScheme") var valueScheme: Map<String, Map<String, Int>>
)

@Serializable
data class SendStatisticsResponse(
    @SerialName("id") val id: Int,
)
