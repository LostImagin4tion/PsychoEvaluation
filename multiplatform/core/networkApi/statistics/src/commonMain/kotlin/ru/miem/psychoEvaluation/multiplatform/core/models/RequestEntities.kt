package ru.miem.psychoEvaluation.multiplatform.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsRequest(
    @SerialName("access_token") val accessToken: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
)


