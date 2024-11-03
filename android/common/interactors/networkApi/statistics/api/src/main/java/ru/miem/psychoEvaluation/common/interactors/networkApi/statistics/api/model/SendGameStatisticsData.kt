package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model

data class SendAirplaneGameStatisticsData(
    val gsrBreathing: List<Int>,
    val gsrGame: List<Int>,
    val gameDuration: Long,
    val gameLevel: Int = 1,
    val date: String,
    val gsrUpperLimit: Float,
    val gsrLowerLimit: Float,
    val timePercentInLimits: Int,
    val timeInLimits: Long,
    val timeAboveUpperLimit: Long,
    val timeUnderLowerLimit: Long,
    val amountOfCrossingLimits: Int,
)

data class SendClocksGameStatisticsData(
    val gsrGame: List<Int>,
    val gameDuration: Long,
    val gameLevel: Int,
    val date: String,
    val gameScore: Int,
    val reactionSpeed: List<Long>,
)
