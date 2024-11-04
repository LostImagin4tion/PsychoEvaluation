package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

import kotlinx.collections.immutable.ImmutableList
import ru.miem.psychoEvaluation.multiplatform.core.models.AirplaneData
import ru.miem.psychoEvaluation.multiplatform.core.models.ClockData

sealed interface DetailedStatisticsState {

    data object Error : DetailedStatisticsState

    data class Success(
        val detailedAirplaneData: ImmutableList<AirplaneData>,
        val detailedClockData: ImmutableList<ClockData>,
    ) : DetailedStatisticsState
}
