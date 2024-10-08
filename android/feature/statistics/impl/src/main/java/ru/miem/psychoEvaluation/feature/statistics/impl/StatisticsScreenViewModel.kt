package ru.miem.psychoEvaluation.feature.statistics.impl

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
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

    val cardUpdate = CardUpdate()

    private fun commonStatistics(
        startDate: String,
        endDate: String
    ) {
        viewModelScope.launch {
            _statisticsState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            statisticsInteractor.commonStatistics(startDate, endDate)
                .run {
                    val uiState = this.toResult<Unit>()
                    Timber.tag(TAG).d("Got new UI state $uiState")
                    _statisticsState.emit(uiState)
                }
        }
    }

    private fun <T> StatisticsState.toResult(): Result<T> = when (this.state) {
        StatisticsResponseType.StatisticAvailable -> SuccessResult()
        StatisticsResponseType.Error -> ErrorResult()
    }

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
    fun onAcceptClick(startDate: MutableState<Date?>, endDate: MutableState<Date?>) {
        if (endDate.value != null) {
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

    companion object {
        val TAG: String = StatisticsScreenViewModel::class.java.simpleName
    }
}
