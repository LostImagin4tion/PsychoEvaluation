package ru.miem.psychoEvaluation.feature.statistics.impl

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.NothingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.Result
import ru.miem.psychoEvaluation.common.designSystem.utils.ResultNames
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.CardUpdate
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.DateProgression
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.parsedApiDate
import timber.log.Timber
import java.time.LocalDate
import java.util.Date

class StatisticsScreenViewModel : ViewModel() {

    private val statisticsInteractor by diApi(StatisticsInteractorDiApi::statisticsInteractor)

    private val _statisticsState = MutableStateFlow<Result<Unit>>(NothingResult())

    val statisticsState: StateFlow<Result<Unit>> = _statisticsState

    val chartModelProducer = CartesianChartModelProducer.build()

    val cardUpdate = CardUpdate(
        this::detailedStatistics,
        this::detailedAirplaneStatistics,
        this::detailedClockStatistics
    )

    private suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): StatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.commonStatistics(startDate, endDate)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            resultState.airplaneData?.let { airplaneData ->
                // Логика обработки airplaneData
                Timber.tag(TAG).d("Airplane Data: $airplaneData")
            }

            resultState.clockData?.let { clockData ->
                // Логика обработки clockData
                Timber.tag(TAG).d("Clock Data: $clockData")
            }
        }

        Timber.tag(TAG).d("Got new UI state $uiState")
        _statisticsState.emit(uiState)
        return resultState
    }

    private fun <T> StatisticsState.toResult(): Result<T> = when (state) {
        StatisticsResponseType.StatisticAvailable -> {
            SuccessResult(
                data = mapOf(
                    "airplaneData" to airplaneData,
                    "clockData" to clockData
                ) as T
            )
        }
        StatisticsResponseType.Error -> ErrorResult()
    }

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
    suspend fun onAcceptClick(
        startDate: MutableState<Date?>,
        endDate: MutableState<Date?>
    ): Pair<Map<String, Int>?, Map<String, Int>?> {
        val resultState = if (endDate.value != null) {
            commonStatistics(
                parsedApiDate(startDate).toString(),
                parsedApiDate(endDate).toString()
            )
        } else {
            commonStatistics(
                parsedApiDate(startDate).toString(),
                parsedApiDate(startDate).toString()
            )
        }

        return Pair(resultState.airplaneData, resultState.clockData)
    }

    suspend fun detailedStatistics(
        xDate: String
    ): DetailedStatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.detailedStatistics(xDate)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            resultState.detailedAirplaneData?.let { airplaneData ->
                // Логика обработки airplaneData
                Timber.tag(TAG).d("Detailed Airplane Data: $airplaneData")
            }

            resultState.detailedClockData?.let { clockData ->
                // Логика обработки clockData
                Timber.tag(TAG).d("Detailed Clock Data: $clockData")
            }
        }
        return resultState
    }

    private fun <T> DetailedStatisticsState.toResult(): Result<T> = when (state) {
        StatisticsResponseType.StatisticAvailable -> {
            SuccessResult(
                data = mapOf(
                    "detailedAirplaneData" to detailedAirplaneData,
                    "detailedClockData" to detailedClockData
                ) as T
            )
        }
        StatisticsResponseType.Error -> ErrorResult()
    }

    suspend fun detailedAirplaneStatistics(
        xLevel: String
    ): DetailedAirplaneStatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.detailedAirplaneStatistics(xLevel)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            Timber.tag(TAG).d("Detailed Airplane Data: ${resultState.gamesAmount} ${resultState.meanDuration}")
        }
        return resultState
    }

    private fun <T> DetailedAirplaneStatisticsState.toResult(): Result<T> = when (state) {
        StatisticsResponseType.StatisticAvailable -> {
            SuccessResult(
                data = mapOf(
                    "meanGsrBreathing" to meanGsrBreathing,
                    "meanGsrGame" to meanGsrGame,
                    "meanDuration" to meanDuration,
                    "gamesAmount" to gamesAmount
                ) as T
            )
        }
        StatisticsResponseType.Error -> ErrorResult()
    }

    suspend fun detailedClockStatistics(
        xLevel: String
    ): DetailedClockStatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.detailedClockStatistics(xLevel)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            Timber.tag(TAG).d("Detailed Clock Data: ${resultState.gamesAmount}")
        }
        return resultState
    }

    private fun <T> DetailedClockStatisticsState.toResult(): Result<T> = when (state) {
        StatisticsResponseType.StatisticAvailable -> {
            SuccessResult(
                data = mapOf(
                    "meanGsrBreathing" to meanGsrBreathing,
                    "meanGsrGame" to meanGsrGame,
                    "meanDuration" to meanDuration,
                    "meanScore" to meanScore,
                    "gamesAmount" to gamesAmount
                ) as T
            )
        }
        StatisticsResponseType.Error -> ErrorResult()
    }

    companion object {
        val TAG: String = StatisticsScreenViewModel::class.java.simpleName
    }
}
