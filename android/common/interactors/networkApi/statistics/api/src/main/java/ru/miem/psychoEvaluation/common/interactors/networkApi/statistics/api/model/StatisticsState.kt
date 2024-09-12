package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

data class StatisticsState(
    val state: StatisticsResponseType
)

enum class StatisticsResponseType {
    StatisticAvailable,
    Error
}
