package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

data class StatisticsCardData(

    val dateRes: String,
    @StringRes val allValueRes: Int,
    val concentrationTimeRes: String,
    val clockTimeRes: String,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit = {},
)