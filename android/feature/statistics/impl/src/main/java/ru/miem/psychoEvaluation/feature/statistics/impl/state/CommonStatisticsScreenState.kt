package ru.miem.psychoEvaluation.feature.statistics.impl.state

import kotlinx.collections.immutable.ImmutableList
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.CommonStatisticsState

sealed interface CommonStatisticsScreenState {

    data object Nothing : CommonStatisticsScreenState
    data object Loading : CommonStatisticsScreenState
    data object Error : CommonStatisticsScreenState

    data class Success(
        val commonStatisticsState: CommonStatisticsState.Success,
        val cardsList: ImmutableList<StatisticsCardData>,
    ) : CommonStatisticsScreenState
}

data class StatisticsCardData(
    val date: String,
    val totalGamesNumber: Int,
    val airplaneCardInfos: ImmutableList<CardTrainingInfo>,
    val clocksCardInfos: ImmutableList<CardTrainingInfo>,
) {
    data class CardTrainingInfo(
        val trainingStart: String,
        val trainingDuration: String,
        val trainingId: Int
    )
}
