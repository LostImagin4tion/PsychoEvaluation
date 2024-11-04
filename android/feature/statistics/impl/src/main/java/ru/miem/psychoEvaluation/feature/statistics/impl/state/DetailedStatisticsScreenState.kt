package ru.miem.psychoEvaluation.feature.statistics.impl.state

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState

sealed interface DetailedStatisticsScreenState {

    data object Nothing : DetailedStatisticsScreenState
    data object Loading : DetailedStatisticsScreenState
    data object Error : DetailedStatisticsScreenState

    data class DetailedAirplaneData(
        val detailedAirplaneStatisticsState: DetailedAirplaneStatisticsState,
    ) : DetailedStatisticsScreenState

    data class DetailedClocksData(
        val detailedClockStatisticsState: DetailedClockStatisticsState,
    ) : DetailedStatisticsScreenState
}
