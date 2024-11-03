package ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl

import kotlinx.coroutines.flow.first
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendAirplaneGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendStatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl.model.toRequest
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.multiplatform.core.di.StatisticsRepositoryDiApi
import ru.miem.psychoEvaluation.multiplatform.core.models.AirplaneData
import ru.miem.psychoEvaluation.multiplatform.core.models.ClockData
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedAirplaneStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedClockStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsAirplaneStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedForLevelsClockStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.DetailedStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendAirplaneGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.SendClocksGameStatisticsRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.StatisticsRequest
import timber.log.Timber
import javax.inject.Inject

class StatisticsInteractorImpl @Inject constructor() : StatisticsInteractor {

    private val dataStore by diApi(DataStorageDiApi::dataStorage)

    private val statisticsRepository by diApi(StatisticsRepositoryDiApi::statisticsRepository)

    override var commonStatisticsAirplane: Map<String, Int>? = null
        private set

    override var commonStatisticsClock: Map<String, Int>? = null
        private set

    private var detailedStatisticsAirplane: List<AirplaneData>? = null
        private set

    private var detailedStatisticsClock: List<ClockData>? = null
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
                    commonStatisticsAirplane = airplane
                    commonStatisticsClock = clock

                    StatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        airplaneData = commonStatisticsAirplane,
                        clockData = commonStatisticsClock
                    )
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
                    detailedStatisticsAirplane = airplane
                    detailedStatisticsClock = clock

                    DetailedStatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        detailedAirplaneData = detailedStatisticsAirplane,
                        detailedClockData = detailedStatisticsClock
                    )
                }
        }
            ?: DetailedStatisticsState(StatisticsResponseType.Error)
    }

    override suspend fun detailedForLevelsAirplaneStatistics(
        xLevel: String
    ): DetailedForLevelsAirplaneStatisticsState {
        val apiAccessToken = dataStore[DataStorageKeys.apiAccessToken].first()
            .takeIf { it.isNotBlank() }
        val requestEntity = DetailedForLevelsAirplaneStatisticsRequest(apiAccessToken.toString(), xLevel)
        return requestEntity.let {
            statisticsRepository.detailedForLevelsAirplaneStatistics(it)
                .also {
                    Timber.tag(TAG).d("Got detailed for levels airplane statistics response $it")
                }
                ?.run {
                    StatisticsState(StatisticsResponseType.StatisticAvailable)

                    DetailedForLevelsAirplaneStatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        meanDuration = meanDuration,
                        gamesAmount = gamesAmount
                    )
                }
        }
            ?: DetailedForLevelsAirplaneStatisticsState(StatisticsResponseType.Error)
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
                    StatisticsState(StatisticsResponseType.StatisticAvailable)

                    DetailedForLevelsClockStatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        meanDuration = meanDuration,
                        meanScore = meanScore,
                        gamesAmount = gamesAmount
                    )
                }
        }
            ?: DetailedForLevelsClockStatisticsState(StatisticsResponseType.Error)
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
                    DetailedAirplaneStatisticsState(StatisticsResponseType.StatisticAvailable)

                    DetailedAirplaneStatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        duration = duration,
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        gsr = gsr,
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
            ?: DetailedAirplaneStatisticsState(StatisticsResponseType.Error)
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
                    DetailedClockStatisticsState(StatisticsResponseType.StatisticAvailable)

                    DetailedClockStatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        duration = duration,
                        meanGsrBreathing = meanGsrBreathing,
                        meanGsrGame = meanGsrGame,
                        gsr = gsr,
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
            ?: DetailedClockStatisticsState(StatisticsResponseType.Error)
    }

    private companion object {
        val TAG: String = StatisticsInteractorImpl::class.java.simpleName
    }
}
