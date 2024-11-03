package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import ru.miem.psychoEvaluation.common.designSystem.utils.NothingResult
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData

data class StatisticsScreenState(
    val state: StatisticsResponseType = StatisticsResponseType.LoadungResult,
    val statisticsState: Pair<StatisticsState?, DetailedStatisticsState?>? = Pair(null, null),
    val bottomSheetState: Pair<DetailedAirplaneStatisticsState?, DetailedClockStatisticsState?>? = Pair(null, null),
    var cardsList:  MutableList<StatisticsCardData?> = mutableListOf(null)
)
