package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

import kotlinx.collections.immutable.ImmutableList

sealed interface DetailedClocksStatisticsState {

    data object Error : DetailedClocksStatisticsState

    data class Success(
        val duration: Int,
        val meanGsrGame: Float,
        val gsr: ImmutableList<Int>,
        val gameId: Int,
        val level: Int,
        val date: String,
        val score: Int,
        val concentrationRate: String,
        val vigilanceRate: String,
        val meanReactionSpeed: Float,
    ) : DetailedClocksStatisticsState
}
