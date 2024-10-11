package ru.miem.psychoEvaluation.feature.statistics.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.statistics.api.StatisticsScreen

interface StatisticsDiApi : DiApi {
    val statisticsScreen: StatisticsScreen
}
