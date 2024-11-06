package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.model

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.multiplatform.core.models.SendAirplaneGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendClocksGameStatisticsRequest

fun SendAirplaneGameStatisticsData.toRequest() = SendAirplaneGameStatisticsRequest(
    gsrBreathing,
    gsrGame,
    gameDuration,
    gameLevel,
    date,
    gsrUpperLimit,
    gsrLowerLimit,
    timePercentInLimits,
    timeInLimits,
    timeAboveUpperLimit,
    timeUnderLowerLimit,
    amountOfCrossingLimits
)

fun SendClocksGameStatisticsData.toRequest() = SendClocksGameStatisticsRequest(
    gsrGame,
    gameDuration,
    gameLevel,
    date,
    gameScore,
    reactionSpeed
)
