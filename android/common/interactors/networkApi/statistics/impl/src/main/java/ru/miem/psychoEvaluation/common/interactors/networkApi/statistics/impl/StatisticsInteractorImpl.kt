package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.first
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.model.toRequest
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.CommonStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedAirplaneStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedClockStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsAirplaneStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsClockStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import timber.log.Timber
import javax.inject.Inject

class StatisticsInteractorImpl @Inject constructor() : StatisticsInteractor {

    private val dataStore by diApi(DataStorageDiApi::dataStorage)

    private val statisticsRepository by diApi(StatisticsRepositoryDiApi::statisticsRepository)

    override suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): CommonStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }

        val requestEntity = StatisticsRequest(apiAccessToken.toString(), startDate, endDate)

        return requestEntity.let {
            statisticsRepository.commonStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got statistics response $it")
                }
                ?.run {
                    CommonStatisticsState.Success(
                        airplaneData = airplaneStatistics.toImmutableMap(),
                        clockData = clockStatistics.toImmutableMap(),
                    )
                }
        }
            ?: CommonStatisticsState.Error
    }

    override suspend fun detailedStatistics(
        xDate: String
    ): DetailedStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }

        val requestEntity = DetailedStatisticsRequest(apiAccessToken.toString(), xDate)

        return requestEntity.let {
            statisticsRepository.detailedStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got detailed statistics response $it")
                }
                ?.run {
                    DetailedStatisticsState.Success(
                        detailedAirplaneData = airplaneStatistics.toImmutableList(),
                        detailedClockData = clockStatistics.toImmutableList(),
                    )
                }
        }
            ?: DetailedStatisticsState.Error
    }

    override suspend fun detailedForLevelsAirplaneStatistics(
        xLevel: String
    ): DetailedForLevelsAirplaneStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }

        val requestEntity = DetailedForLevelsAirplaneStatisticsRequest(
            apiAccessToken.toString(),
            xLevel
        )

        return requestEntity.let {
            statisticsRepository.detailedForLevelsAirplaneStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got detailed for levels airplane statistics response $it")
                }
                ?.run {
                    DetailedForLevelsAirplaneStatisticsState.Success(
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        meanDuration = meanDuration,
                        gamesAmount = gamesAmount
                    )
                }
        }
            ?: DetailedForLevelsAirplaneStatisticsState.Error
    }

    override suspend fun detailedForLevelsClockStatistics(
        xLevel: String
    ): DetailedForLevelsClockStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }

        val requestEntity = DetailedForLevelsClockStatisticsRequest(apiAccessToken.toString(), xLevel)

        return requestEntity.let {
            statisticsRepository.detailedForLevelsClockStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got detailed for levels clock statistics response $it")
                }
                ?.run {
                    DetailedForLevelsClockStatisticsState.Success(
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        meanDuration = meanDuration,
                        meanScore = meanScore,
                        gamesAmount = gamesAmount
                    )
                }
        }
            ?: DetailedForLevelsClockStatisticsState.Error
    }

    override suspend fun detailedAirplaneStatistics(
        gameId: String
    ): DetailedAirplaneStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }

        val requestEntity = DetailedAirplaneStatisticsRequest(apiAccessToken.toString(), gameId)

        return requestEntity.let {
            statisticsRepository.detailedAirplaneStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got detailed airplane statistics response $it")
                }
                ?.run {
                    DetailedAirplaneStatisticsState.Success(
                        duration = duration,
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        gsr = gsr.toImmutableList(),
                        gameId = gameId.toInt(),
                        level = level,
                        date = date,
                        gsrUpperLimit = gsrUpperLimit,
                        gsrLowerLimit = gsrLowerLimit,
                        timePercentInLimits = timePercentInLimits,
                        timeInLimits = timeInLimits,
                        timeAboveUpperLimit = timeAboveUpperLimit,
                        timeUnderLowerLimit = timeUnderLowerLimit,
                        amountOfCrossingLimits = amountOfCrossingLimits
                    )
                }
        }
            ?: DetailedAirplaneStatisticsState.Error
    }

    override suspend fun detailedClockStatistics(
        gameId: String
    ): DetailedClockStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
        val requestEntity = DetailedClockStatisticsRequest(apiAccessToken.toString(), gameId)
        return requestEntity.let {
            statisticsRepository.detailedClockStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got detailed clock statistics response $it")
                }
                ?.run {
                    DetailedClockStatisticsState.Success(
                        duration = duration,
                        meanReactionSpeed = meanReactionSpeed,
                        meanGsrGame = meanGsrGame,
                        gsr = gsr.toImmutableList(),
                        gameId = gameId.toInt(),
                        level = level,
                        date = date,
                        score = score,
                        concentrationRate = concentrationRate,
                        vigilanceRate = vigilanceRate
                    )
                }
        }
            ?: DetailedClockStatisticsState.Error
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
