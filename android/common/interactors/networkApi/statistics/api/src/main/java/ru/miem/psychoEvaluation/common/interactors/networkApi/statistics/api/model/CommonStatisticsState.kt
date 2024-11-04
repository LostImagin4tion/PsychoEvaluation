package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

import kotlinx.collections.immutable.ImmutableMap

sealed interface CommonStatisticsState {

    data object Error : CommonStatisticsState

    data class Success(
        val airplaneData: ImmutableMap<String, Int>,
        val clockData: ImmutableMap<String, Int>,
    ) : CommonStatisticsState
}
