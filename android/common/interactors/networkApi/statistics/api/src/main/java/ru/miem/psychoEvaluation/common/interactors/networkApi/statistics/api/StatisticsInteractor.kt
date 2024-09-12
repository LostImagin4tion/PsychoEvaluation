package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState

interface StatisticsInteractor {
    val valueScheme: Map<String, Map<String, Int>>?

    suspend fun common_statistics(
        apiAccessToken: String,
        startDate: String,
        endDate: String
    ): StatisticsState
}
