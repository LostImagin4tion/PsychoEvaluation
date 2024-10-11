package ru.miem.psychoEvaluation.multiplatform.core.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.multiplatform.core.StatisticsRepository

interface StatisticsRepositoryDiApi : DiApi {
    val statisticsRepository: StatisticsRepository
}
