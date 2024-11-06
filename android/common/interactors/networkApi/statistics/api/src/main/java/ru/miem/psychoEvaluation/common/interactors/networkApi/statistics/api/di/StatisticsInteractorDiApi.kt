package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface StatisticsInteractorDiApi : DiApi {
    val statisticsInteractor: StatisticsInteractor
}
