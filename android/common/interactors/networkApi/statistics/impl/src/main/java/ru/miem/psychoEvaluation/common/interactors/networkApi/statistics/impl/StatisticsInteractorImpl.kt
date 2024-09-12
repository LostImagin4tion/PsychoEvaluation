package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl

import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import timber.log.Timber
import javax.inject.Inject

class StatisticsInteractorImpl @Inject constructor() : StatisticsInteractor {

//    private val dataStore by diApi(DataStorageDiApi::dataStorage)
    private val statisticsRepository by diApi(StatisticsRepositoryDiApi::statisticsRepository)

    override var valueScheme: Map<String, Map<String, Int>>? = null
        private set

    override suspend fun common_statistics(
        apiAccessToken: String,
        startDate: String,
        endDate: String
    ): StatisticsState {
        val requestEntity = StatisticsRequest(apiAccessToken, startDate, endDate)
        return requestEntity?.let {
            statisticsRepository.commonStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got statistics response $it")
                }
                ?.run {
                    valueScheme = valuescheme
                    StatisticsState(StatisticsResponseType.StatisticAvailable)
                }
        }
            ?: StatisticsState(StatisticsResponseType.Error)
    }

    private companion object {
        val TAG: String = StatisticsInteractorImpl::class.java.simpleName
    }
}
