package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

sealed interface DetailedForLevelsAirplaneStatisticsState {

    data object Error : DetailedForLevelsAirplaneStatisticsState

    data class Success(
        val meanGsrBreathing: Float,
        val meanGsrGame: Float,
        val meanDuration: Float,
        val gamesAmount: Float,
    ) : DetailedForLevelsAirplaneStatisticsState
}
