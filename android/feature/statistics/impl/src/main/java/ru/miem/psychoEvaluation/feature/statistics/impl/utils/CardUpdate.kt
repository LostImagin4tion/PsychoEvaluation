package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import androidx.compose.runtime.MutableState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.feature.statistics.impl.StatisticsScreenViewModel
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class CardUpdate(
    private val detailedStatistics: suspend (String) -> DetailedStatisticsState,
    private val detailedAirplaneStatistics: suspend (String) -> DetailedAirplaneStatisticsState,
    private val detailedClockStatistics: suspend (String) -> DetailedClockStatisticsState
) {

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    suspend fun onUpdateCards(
        data1: MutableState<Date?>,
        data2: MutableState<Date?>
    ): MutableList<StatisticsCardData?> {
        val cardsList: MutableList<StatisticsCardData?> = mutableListOf(null)
        if (data2.value != null && data1.value != null) {
            val date1 = data1.value?.getParsedDate("yyyy-MM-dd")
            val date2 = data2.value?.getParsedDate("yyyy-MM-dd")
            for (i in LocalDate.parse(date1)..LocalDate.parse(date2) step 1) {
                val dateString = i.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                val detailedStats = detailedStatistics(dateString)

                val airplaneIds = detailedStats.detailedAirplaneData?.map { it.level }
                val clockIds = detailedStats.detailedClockData?.map { it.level }

                Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
                Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

                getInfo(cardsList, i, airplaneIds, clockIds)
            }
        } else if (data1.value == null) {
            val dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

            val detailedStats = detailedStatistics(dateString)

            val airplaneIds = detailedStats.detailedAirplaneData?.map { it.level }
            val clockIds = detailedStats.detailedClockData?.map { it.level }

            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

            getInfo(cardsList, LocalDateTime.now().toLocalDate(), airplaneIds, clockIds)
        } else {
            val date1 = data1.value!!.getParsedDate("d MMMM")

            val dateString = data1.value!!.getParsedDate("yyyy.MM.dd")

            val detailedStats = detailedStatistics(dateString)

            val airplaneIds = detailedStats.detailedAirplaneData?.map { it.level }
            val clockIds = detailedStats.detailedClockData?.map { it.level }

            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

            getInfo(cardsList, LocalDate.parse(dateString), airplaneIds, clockIds)
        }
        return cardsList
    }

    fun formatSecondsToMinutes(seconds: Float?): String {
        val seconds1 = seconds?.toInt()
        if (seconds1 != 0) {
            val minutes = seconds1?.div(SECONDS_IN_MINUTE)
            val remainingSeconds = seconds1?.rem(SECONDS_IN_MINUTE)
            return String.format(Locale.getDefault(), "%d:%02d", minutes, remainingSeconds)
        } else {
            return "0:00"
        }
    }

    private suspend fun getInfo(
        cardsList: MutableList<StatisticsCardData?>,
        date: LocalDate,
        airplaneIds: List<Int>?,
        clockIds: List<Int>?
    ) {
        var totalAirplaneMeanDuration = 0f
        if (airplaneIds != null) {
            for (airplaneId in airplaneIds) {
                val airplaneStats = detailedAirplaneStatistics(airplaneId.toString())
                totalAirplaneMeanDuration += airplaneStats.meanDuration ?: 0f
            }
        }

        var totalClockMeanDuration = 0f
        if (clockIds != null) {
            for (clockId in clockIds) {
                val clockStats = detailedClockStatistics(clockId.toString())
                totalClockMeanDuration += clockStats.meanDuration ?: 0f
            }
        }

        val totalGames = (airplaneIds?.size ?: 0) + (clockIds?.size ?: 0)

        val airplaneDuration = formatSecondsToMinutes(totalAirplaneMeanDuration)

        val clockDuration = formatSecondsToMinutes(totalClockMeanDuration)

        cardsList.add(
            StatisticsCardData(
                date.format(DateTimeFormatter.ofPattern("d MMMM")),
                totalGames, airplaneDuration, clockDuration
            )
        )
    }
    companion object {
        private const val SECONDS_IN_MINUTE = 60
    }
}
