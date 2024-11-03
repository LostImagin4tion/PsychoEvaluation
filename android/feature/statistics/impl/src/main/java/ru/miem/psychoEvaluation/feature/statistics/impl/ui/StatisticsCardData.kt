package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.annotation.StringRes

data class StatisticsCardData(
    val dateRes: String,
    @StringRes val allValueRes: Int,
    val concentrationTrainingsValue: MutableList<Triple<String, String, Int>>,
    val clockTrainingsValue: MutableList<Triple<String, String, Int>>
)
