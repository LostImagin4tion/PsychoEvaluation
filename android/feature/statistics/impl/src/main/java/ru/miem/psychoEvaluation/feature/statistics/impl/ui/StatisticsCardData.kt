package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.annotation.StringRes

data class StatisticsCardData(

    val dateRes: String,
    @StringRes val allValueRes: Int,
    val concentrationTimeRes: String,
    val clockTimeRes: String

)
