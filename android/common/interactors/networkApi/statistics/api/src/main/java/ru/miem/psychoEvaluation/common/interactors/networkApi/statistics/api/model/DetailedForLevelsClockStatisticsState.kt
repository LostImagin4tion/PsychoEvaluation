package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

interface DetailedForLevelsClockStatisticsState {

    data object Error : DetailedForLevelsClockStatisticsState

    data class Success(
        val meanGsrBreathing: Float,
        val meanGsrGame: Float,
        val meanDuration: Float,
        val meanScore: Float,
        val gamesAmount: Float,
    ) : DetailedForLevelsClockStatisticsState
}
