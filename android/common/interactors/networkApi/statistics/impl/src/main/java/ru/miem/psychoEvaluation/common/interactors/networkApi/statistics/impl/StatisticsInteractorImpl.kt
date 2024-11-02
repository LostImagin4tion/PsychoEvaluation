package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl

import kotlinx.coroutines.flow.first
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.model.toRequest
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import timber.log.Timber
import javax.inject.Inject

class StatisticsInteractorImpl @Inject constructor() : StatisticsInteractor {

    private val dataStore by diApi(DataStorageDiApi::dataStorage)

    private val statisticsRepository by diApi(StatisticsRepositoryDiApi::statisticsRepository)

    override var commonStatisticsValueScheme: Map<String, Map<String, Int>>? = null
        private set

    override suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): StatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
        val requestEntity = StatisticsRequest(apiAccessToken.toString(), startDate, endDate)
        return requestEntity.let {
            statisticsRepository.commonStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got statistics response $it")
                }
                ?.run {
                    commonStatisticsValueScheme = valueScheme
                    StatisticsState(StatisticsResponseType.StatisticAvailable)
                }
        }
            ?: StatisticsState(StatisticsResponseType.Error)
    }

    override suspend fun sendAirplaneGameStatistics(
        data: SendAirplaneGameStatisticsData
    ): SendStatisticsResponseType {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
            ?: return SendStatisticsResponseType.AccessTokenExpired

        return statisticsRepository.sendAirplaneStatistics(data.toRequest(), apiAccessToken)
            .also { Timber.tag(TAG).d("Got send airplane statistics response $it") }
            .let { isSuccessful ->
                if (isSuccessful) {
                    SendStatisticsResponseType.Success
                } else {
                    SendStatisticsResponseType.Failed
                }
            }
    }

    override suspend fun sendClocksGameStatistics(
        data: SendClocksGameStatisticsData
    ): SendStatisticsResponseType {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
            ?: return SendStatisticsResponseType.AccessTokenExpired

        return statisticsRepository.sendClocksStatistics(data.toRequest(), apiAccessToken)
            .also { Timber.tag(TAG).d("Got send airplane statistics response $it") }
            .let { isSuccessful ->
                if (isSuccessful) {
                    SendStatisticsResponseType.Success
                } else {
                    SendStatisticsResponseType.Failed
                }
            }
    }

    private companion object {
        val TAG: String = StatisticsInteractorImpl::class.java.simpleName
    }
}
