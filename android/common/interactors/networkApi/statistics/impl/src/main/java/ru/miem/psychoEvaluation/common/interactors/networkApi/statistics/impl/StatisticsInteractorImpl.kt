package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl

import kotlinx.coroutines.flow.first
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.SendAirplaneGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendClocksGameStatisticsRequest
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
        gsrBreathing: List<Int>,
        gsrGame: List<Int>,
        duration: Int,
        date: String,
    ): SendStatisticsResponseType {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
            ?: return SendStatisticsResponseType.AccessTokenExpired

        val requestEntity = SendAirplaneGameStatisticsRequest(
            gsrBreathing = gsrBreathing,
            gsrGame = gsrGame,
            gameDuration = duration,
            gameLevel = 1,
            date = date,
        )

        return statisticsRepository.sendAirplaneStatistics(requestEntity, apiAccessToken)
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
        gsrBreathing: List<Int>,
        gsrGame: List<Int>,
        duration: Int,
        level: Int,
        date: String,
        score: Int,
    ): SendStatisticsResponseType {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
            ?: return SendStatisticsResponseType.AccessTokenExpired

        val requestEntity = SendClocksGameStatisticsRequest(
            gsrBreathing = gsrBreathing,
            gsrGame = gsrGame,
            gameDuration = duration,
            gameLevel = level,
            date = date,
            gameScore = score,
        )

        return statisticsRepository.sendClocksStatistics(requestEntity, apiAccessToken)
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
