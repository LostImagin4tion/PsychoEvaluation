package ru.miem.psychoEvaluation.feature.statistics.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.CommonStatisticsState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.statistics.impl.state.CommonStatisticsScreenState
import ru.miem.psychoEvaluation.feature.statistics.impl.state.DetailedStatisticsScreenState
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartController
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.StatisticsCardsCreator
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.parsedDateForApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class StatisticsScreenViewModel : ViewModel() {

    private val statisticsInteractor by diApi(StatisticsInteractorDiApi::statisticsInteractor)

    private val _commonStatisticsScreenState =
        MutableStateFlow<CommonStatisticsScreenState>(CommonStatisticsScreenState.Nothing)
    private val _detailedStatisticsScreenState =
        MutableStateFlow<DetailedStatisticsScreenState>(DetailedStatisticsScreenState.Nothing)

    private val statisticsCardsCreator by lazy { StatisticsCardsCreator(statisticsInteractor) }
    private val chartController by lazy { ChartController(chartModelProducer) }

    val commonStatisticsScreenStateStateFlow: StateFlow<CommonStatisticsScreenState> = _commonStatisticsScreenState
    val detailedStatisticsScreenState: StateFlow<DetailedStatisticsScreenState> = _detailedStatisticsScreenState

    val chartModelProducer = CartesianChartModelProducer.build()

    val labelListKey: ExtraStore.Key<Map<Int, LocalDate>>
        get() = chartController.labelListKey

    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues, _ ->
            val days = chartValues.model.extraStore[chartController.labelListKey][value.toInt()]
                ?: LocalDate.ofEpochDay(value.toLong())

            days.format(dateTimeFormatter)
        }

    fun requestCommonStatistics(
        startDate: Date,
        endDate: Date,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _commonStatisticsScreenState.emit(CommonStatisticsScreenState.Loading)

            when (val commonStatistics = commonStatistics(startDate, endDate)) {
                is CommonStatisticsState.Success -> {
                    chartController.updateChart(startDate, endDate, commonStatistics)

                    val cardsList = statisticsCardsCreator.createCards(startDate, endDate)

                    _commonStatisticsScreenState.run {
                        emit(
                            CommonStatisticsScreenState.Success(
                                commonStatisticsState = commonStatistics,
                                cardsList = cardsList,
                            )
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun resetCommonStatisticsScreenState() {
        viewModelScope.launch {
            _commonStatisticsScreenState.emit(CommonStatisticsScreenState.Nothing)
        }
    }

    fun resetDetailedStatisticsScreenState() {
        viewModelScope.launch {
            _detailedStatisticsScreenState.emit(DetailedStatisticsScreenState.Nothing)
        }
    }

    fun onTrainingSelected(
        trainingId: String,
        trainingType: String
    ) {
        viewModelScope.launch {
            if (trainingType == "airplane") {
                val airplaneStats = statisticsInteractor.detailedAirplaneStatistics(trainingId)

                _detailedStatisticsScreenState.emit(
                    DetailedStatisticsScreenState.DetailedAirplaneData(airplaneStats)
                )
            } else {
                val clockStats = statisticsInteractor.detailedClockStatistics(trainingId)

                _detailedStatisticsScreenState.emit(
                    DetailedStatisticsScreenState.DetailedClocksData(clockStats)
                )
            }
        }
    }

    private suspend fun commonStatistics(
        startDate: Date,
        endDate: Date,
    ): CommonStatisticsState {
        return statisticsInteractor.commonStatistics(
            parsedDateForApi(startDate),
            parsedDateForApi(endDate),
        )
    }

    companion object {
        val TAG: String = StatisticsScreenViewModel::class.java.simpleName

        private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d")
    }
}
