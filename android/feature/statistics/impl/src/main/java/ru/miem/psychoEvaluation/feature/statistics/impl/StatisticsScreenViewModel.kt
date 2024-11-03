package ru.miem.psychoEvaluation.feature.statistics.impl

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.NothingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.Result
import ru.miem.psychoEvaluation.common.designSystem.utils.ResultNames
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedForLevelsClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.CardUpdate
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartUpdate
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.DateProgression
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.StatisticsScreenState
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.parsedApiDate
import timber.log.Timber
import java.time.LocalDate
import java.util.Date

class StatisticsScreenViewModel : ViewModel() {

    private val statisticsInteractor by diApi(StatisticsInteractorDiApi::statisticsInteractor)

    private val _statisticsState = MutableStateFlow<Result<Unit>>(NothingResult())

    private val _screenState = MutableStateFlow(StatisticsScreenState())

    val screenState: StateFlow<StatisticsScreenState> = _screenState

    private val _detailedStatisticsState = MutableStateFlow<Result<Unit>>(NothingResult())

    val chartModelProducer = CartesianChartModelProducer.build()

    val cardUpdate = CardUpdate(
        this::detailedStatistics
    )

    var сommonStatisticsState: StatisticsState? = null

    private suspend fun commonStatistics(
        startDate: String,
        endDate: String
    ): StatisticsState? {
        val job = viewModelScope.launch {
            _statisticsState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            сommonStatisticsState = statisticsInteractor.commonStatistics(startDate, endDate)

            // Обрабатываем и передаем данные из interactor в UI state
            val uiState = сommonStatisticsState!!.toResult<Unit>().apply {
                сommonStatisticsState!!.airplaneData?.let { airplaneData ->
                    // Логика обработки airplaneData
                    Timber.tag(TAG).d("Airplane Data: $airplaneData")
                }

                сommonStatisticsState!!.clockData?.let { clockData ->
                    // Логика обработки clockData
                    Timber.tag(TAG).d("Clock Data: $clockData")
                }
                _statisticsState.emit(SuccessResult())
            }

            Timber.tag(TAG).d("Got new AAA UI state $uiState")

        }
        job.join()
        Log.d("сommonStatisticsState", сommonStatisticsState.toString())
        return сommonStatisticsState
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
        else -> LoadingResult()
    }

    fun resetState() {
        viewModelScope.launch {
            _statisticsState.emit(NothingResult())
            _detailedStatisticsState.emit(NothingResult())
        }
    }
    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    var resultCommonStatisticsState: StatisticsState? = null
    suspend fun onAcceptClick(
        startDate: MutableState<Date?>,
        endDate: MutableState<Date?>
    ): Pair<Map<String, Int>?, Map<String, Int>?> {
        val loadDataJob = viewModelScope.launch {
            resultCommonStatisticsState = if (endDate.value != null) {
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
        }
        loadDataJob.join()

    return Pair(
        resultCommonStatisticsState?.airplaneData,
        resultCommonStatisticsState?.clockData
    )
    }

    var resultDetailedStatisticsState: DetailedStatisticsState? = null

    private suspend fun detailedStatistics(
        xDate: String
    ): DetailedStatisticsState? {
        val job = viewModelScope.launch {
            _detailedStatisticsState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            resultDetailedStatisticsState = statisticsInteractor.detailedStatistics(xDate)

            // Обрабатываем и передаем данные из interactor в UI state
            val uiState = resultDetailedStatisticsState!!.toResult<Unit>().apply {
                resultDetailedStatisticsState!!.detailedAirplaneData?.let { airplaneData ->
                    // Логика обработки airplaneData
                    Timber.tag(TAG).d("Detailed Airplane Data: $airplaneData")
                }

                resultDetailedStatisticsState!!.detailedClockData?.let { clockData ->
                    // Логика обработки clockData
                    Timber.tag(TAG).d("Detailed Clock Data: $clockData")
                }
                _detailedStatisticsState.emit(SuccessResult())
            }
            Timber.tag(TAG).d("Got new UI detailed state $uiState")
        }
        job.join()
        return resultDetailedStatisticsState
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
        else -> LoadingResult()
    }

    suspend fun detailedForLevelsAirplaneStatistics(
        xLevel: String
    ): DetailedForLevelsAirplaneStatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.detailedForLevelsAirplaneStatistics(xLevel)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            Timber.tag(TAG).d("Detailed Airplane Data: ${resultState.gamesAmount} ${resultState.meanDuration}")
        }
        return resultState
    }

    private fun <T> DetailedForLevelsAirplaneStatisticsState.toResult(): Result<T> = when (state) {
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
        else -> LoadingResult()
    }

    suspend fun detailedForLevelsClockStatistics(
        xLevel: String
    ): DetailedForLevelsClockStatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.detailedForLevelsClockStatistics(xLevel)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            Timber.tag(TAG).d("Detailed Clock Data: ${resultState.gamesAmount}")
        }
        return resultState
    }

    private fun <T> DetailedForLevelsClockStatisticsState.toResult(): Result<T> = when (state) {
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
        else -> LoadingResult()
    }

    var resultDetailedAirplaneStatisticsState: DetailedAirplaneStatisticsState? = null
    fun detailedAirplaneStatistics(
        gameId: String
    ): DetailedAirplaneStatisticsState? {
        viewModelScope.launch {
            _statisticsState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            resultDetailedAirplaneStatisticsState = statisticsInteractor.detailedAirplaneStatistics(gameId)

            // Обрабатываем и передаем данные из interactor в UI state
            val uiState = resultDetailedAirplaneStatisticsState!!.toResult<Unit>().apply {
                Timber.tag(TAG)
                    .d("Detailed Airplane Data: ${resultDetailedAirplaneStatisticsState!!.duration} ${resultDetailedAirplaneStatisticsState!!.gsrUpperLimit}")
            }
        }
        return resultDetailedAirplaneStatisticsState
    }

    private fun <T> DetailedAirplaneStatisticsState.toResult(): Result<T> = when (state) {
        StatisticsResponseType.StatisticAvailable -> {
            SuccessResult(
                data = mapOf(
                    "duration" to duration,
                    "meanGsrBreathing" to meanGsrBreathing,
                    "meanGsrGame" to meanGsrGame,
                    "gsr" to gsr,
                    "gameId" to gameId,
                    "level" to level,
                    "date" to date,
                    "gsrUpperLimit" to gsrUpperLimit,
                    "gsrLowerLimit" to gsrLowerLimit,
                    "timePercentInLimits" to timePercentInLimits,
                    "timeInLimits" to timeInLimits,
                    "timeAboveUpperLimit" to timeAboveUpperLimit,
                    "timeUnderLowerLimit" to timeUnderLowerLimit,
                    "amountOfCrossingLimits" to amountOfCrossingLimits
                ) as T
            )
        }
        StatisticsResponseType.Error -> ErrorResult()
        else -> LoadingResult()
    }

    suspend fun detailedClockStatistics(
        gameId: String
    ): DetailedClockStatisticsState {
        _statisticsState.emit(LoadingResult())
        Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

        val resultState = statisticsInteractor.detailedClockStatistics(gameId)

        // Обрабатываем и передаем данные из interactor в UI state
        val uiState = resultState.toResult<Unit>().apply {
            Timber.tag(TAG).d("Detailed Clock Data: ${resultState.duration} ${resultState.gsrUpperLimit}")
        }
        return resultState
    }

    private fun <T> DetailedClockStatisticsState.toResult(): Result<T> = when (state) {
        StatisticsResponseType.StatisticAvailable -> {
            SuccessResult(
                data = mapOf(
                    "duration" to duration,
                    "meanGsrBreathing" to meanGsrBreathing,
                    "meanGsrGame" to meanGsrGame,
                    "gsr" to gsr,
                    "gameId" to gameId,
                    "level" to level,
                    "date" to date,
                    "gsrUpperLimit" to gsrUpperLimit,
                    "gsrLowerLimit" to gsrLowerLimit,
                    "timePercentInLimits" to timePercentInLimits,
                    "timeInLimits" to timeInLimits,
                    "timeAboveUpperLimit" to timeAboveUpperLimit,
                    "timeUnderLowerLimit" to timeUnderLowerLimit,
                    "amountOfCrossingLimits" to amountOfCrossingLimits
                ) as T
            )
        }
        StatisticsResponseType.Error -> ErrorResult()
        else -> LoadingResult()
    }

    fun loadStatisticsData(chart: ChartUpdate, startDate: MutableState<Date?>, endDate: MutableState<Date?>) {
        var data: Pair<Map<String, Int>?, Map<String, Int>?> = Pair(null,null)
        var cardsList: MutableList<StatisticsCardData?> = mutableListOf()

        viewModelScope.launch(Dispatchers.IO) {
            // Устанавливаем состояние загрузки
            _screenState.emit(
                _screenState.value.copy(
                    state = StatisticsResponseType.LoadungResult,
                    statisticsState = Pair(
                        StatisticsState(state = StatisticsResponseType.LoadungResult),
                        null
                    ),
                    bottomSheetState = null,
                    cardsList = mutableListOf()
                )
            )
            data = onAcceptClick(startDate, endDate)

            Log.d("COROT", "FIRST")
            chart.onUpdateChart(startDate, endDate, data, chart)
            Log.d("COROT", "SECOND")
            cardsList =
                cardUpdate.onUpdateCards(startDate, endDate) // Список для хранения карточек


            Log.d("COROT", "THIRD")
            Log.d("DATA", data.toString())
            Log.d("CARDS", cardsList.toString())


                    // Создаем состояние StatisticsState с общими данными или состоянием ошибки
            val basicStatisticsState = when {
                _statisticsState.value is SuccessResult-> {
                    StatisticsState(
                        state = StatisticsResponseType.StatisticAvailable,
                        airplaneData = data.first,
                        clockData = data.second
                    )
                }
                else -> {
                    StatisticsState(state = StatisticsResponseType.LoadungResult)
                }
            }
            _screenState.emit(
                _screenState.value.copy(
                    state = basicStatisticsState.state,
                    statisticsState = Pair(basicStatisticsState, null),
                    cardsList = cardsList
                )
            )
        }
    }

    // Функция для загрузки данных тренировки и отображения в bottomSheet при нажатии
    fun onTrainingSelected(trainingId: String) {
        viewModelScope.launch {
            // Загружаем данные для выбранной тренировки
            val airplaneStats = detailedAirplaneStatistics(trainingId)
            val clockStats = detailedClockStatistics(trainingId)

            // Обновляем bottomSheetState, если данные успешно получены
            val bottomSheetData = if (airplaneStats != null && clockStats != null) {
                Pair(airplaneStats, clockStats)
            } else {
                Pair(null, null) // Очищаем, если произошла ошибка
            }

            _screenState.emit(
                _screenState.value.copy(bottomSheetState = bottomSheetData)
            )
        }
    }

     fun <T> toStatResult(it:StatisticsScreenState): Result<T> {
        val (statisticsState, detailedStatisticsState) = it.statisticsState ?: Pair(null, null)
        val (detailedAirplaneState, detailedClockState) = it.bottomSheetState ?: Pair(null, null)
         Timber.tag(TAG).d("Got new UI JJJ state ${it.state}")
        return when (it.state) {
            StatisticsResponseType.StatisticAvailable -> {
                SuccessResult(
                    data = mapOf(
                        "statisticsState" to Pair(statisticsState, detailedStatisticsState),
                        "bottomSheetState" to Pair(detailedAirplaneState, detailedClockState),
                    ) as T
                )
            }
            StatisticsResponseType.Error -> ErrorResult()
            else -> LoadingResult()
        }
    }

    companion object {
        val TAG: String = StatisticsScreenViewModel::class.java.simpleName
    }
}
