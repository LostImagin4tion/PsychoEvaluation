package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.compose.material.ModalBottomSheetState
import kotlinx.coroutines.CoroutineScope

data class DetailedSheetAirplaneData(
    val duration: Int?,
    val meanGsrBreathing: Float?,
    val meanGsrGame: Float?,
    val gsr: List<Int>?,
    val gameId: Int?,
    val level: Int?,
    val date: String?,
    val gsrUpperLimit: Float?,
    val gsrLowerLimit: Float?,
    val timePercentInLimits: Int?,
    val timeInLimits: Int?,
    val timeAboveUpperLimit: Int?,
    val timeUnderLowerLimit: Int?,
    val amountOfCrossingLimits: Int?
)

data class DetailedSheetClockData(
    val duration: Int?,
    val meanGsrBreathing: Float?,
    val meanGsrGame: Float?,
    val gsr: List<Int>?,
    val gameId: Int?,
    val level: Int?,
    val date: String?,
    val gsrUpperLimit: Float?,
    val gsrLowerLimit: Float?,
    val timePercentInLimits: Int?,
    val timeInLimits: Int?,
    val timeAboveUpperLimit: Int?,
    val timeUnderLowerLimit: Int?,
    val amountOfCrossingLimits: Int?
)

