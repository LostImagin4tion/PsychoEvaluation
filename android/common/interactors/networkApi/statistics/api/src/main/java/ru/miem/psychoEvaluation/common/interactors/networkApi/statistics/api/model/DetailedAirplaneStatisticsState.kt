package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

import kotlinx.collections.immutable.ImmutableList

sealed interface DetailedAirplaneStatisticsState {

    data object Error : DetailedAirplaneStatisticsState

    data class Success(
        val duration: Int,
        val meanGsrBreathing: Float,
        val meanGsrGame: Float,
        val gsr: ImmutableList<Int>,
        val gameId: Int,
        val level: Int,
        val date: String,
        val gsrUpperLimit: Float,
        val gsrLowerLimit: Float,
        val timePercentInLimits: Int,
        val timeInLimits: Int,
        val timeAboveUpperLimit: Int,
        val timeUnderLowerLimit: Int,
        val amountOfCrossingLimits: Int,
    ) : DetailedAirplaneStatisticsState
}
