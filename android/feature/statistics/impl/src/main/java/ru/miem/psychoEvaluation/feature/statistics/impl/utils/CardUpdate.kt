package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.feature.statistics.impl.StatisticsScreenViewModel
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import ru.miem.psychoEvaluation.multiplatform.core.models.AirplaneData
import ru.miem.psychoEvaluation.multiplatform.core.models.ClockData
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.reflect.KSuspendFunction1

class CardUpdate(
    private val detailedStatistics: KSuspendFunction1<String, DetailedStatisticsState?>
) {

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

     suspend fun onUpdateCards(
        data1: MutableState<Date?>,
        data2: MutableState<Date?>
    ): MutableList<StatisticsCardData?> {
        val cardsList: MutableList<StatisticsCardData?> = mutableListOf()
        if (data2.value != null && data1.value != null) {
            val date1 = data1.value?.getParsedDate("yyyy-MM-dd")
            val date2 = data2.value?.getParsedDate("yyyy-MM-dd")
            for (i in LocalDate.parse(date1)..LocalDate.parse(date2) step 1) {
                val dateString = i.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                val detailedStats = detailedStatistics(dateString) ?: DetailedStatisticsState(StatisticsResponseType.StatisticAvailable, mutableListOf<AirplaneData>(), mutableListOf<ClockData>())

                val airplaneIds = detailedStats?.detailedAirplaneData?.map { it.id }
                val clockIds = detailedStats?.detailedClockData?.map { it.id }

                Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
                Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

                if (detailedStats != null) {
                    getInfo(cardsList, i, airplaneIds, clockIds, detailedStats)
                }
            }
        } else if (data1.value == null) {
            val dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

            val detailedStats = detailedStatistics(dateString) ?: DetailedStatisticsState(StatisticsResponseType.StatisticAvailable, mutableListOf<AirplaneData>(), mutableListOf<ClockData>())

            val airplaneIds = detailedStats?.detailedAirplaneData?.map { it.id }
            val clockIds = detailedStats?.detailedClockData?.map { it.id }

            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

            if (detailedStats != null) {
                getInfo(
                    cardsList,
                    LocalDateTime.now().toLocalDate(),
                    airplaneIds,
                    clockIds,
                    detailedStats
                )
            }
        } else {
            val date1 = data1.value!!.getParsedDate("d MMMM")

            val dateString = data1.value!!.getParsedDate("yyyy.MM.dd")

            val detailedStats = detailedStatistics(dateString) ?: DetailedStatisticsState(StatisticsResponseType.StatisticAvailable, mutableListOf<AirplaneData>(), mutableListOf<ClockData>())

            val dateString1 = data1.value!!.getParsedDate("yyyy-MM-dd")

            val airplaneIds = detailedStats?.detailedAirplaneData?.map { it.id }
            val clockIds = detailedStats?.detailedClockData?.map { it.id }

            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

            if (detailedStats != null) {
                getInfo(cardsList, LocalDate.parse(dateString1), airplaneIds, clockIds, detailedStats)
            }

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

    private fun getInfo(
        cardsList: MutableList<StatisticsCardData?>,
        date: LocalDate,
        airplaneIds: List<Int>?,
        clockIds: List<Int>?,
        detailedStats: DetailedStatisticsState
    ) {
        val totalAirplaneDurations: MutableList<Triple<String, String, Int>> = mutableListOf()

        Log.d("STATS", detailedStats.toString())

        if (airplaneIds != null) {
            for (airplaneId in airplaneIds) {
                val meanDuration = detailedStats.detailedAirplaneData
                    ?.find { it.id == airplaneId }
                    ?.duration?.toFloat() ?: 0f

                val trainingStart = detailedStats.detailedAirplaneData
                    ?.find { it.id == airplaneId }
                    ?.date
                    ?.let { LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")) }
                    ?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Неизвестно"

                totalAirplaneDurations.add(Triple(trainingStart, formatSecondsToMinutes(meanDuration), airplaneId))
            }
        }

        val totalClockDurations: MutableList<Triple<String, String, Int>> = mutableListOf()

        if (clockIds != null) {
            for (clockId in clockIds) {
                val meanDuration = detailedStats.detailedClockData
                    ?.find { it.id == clockId }
                    ?.duration?.toFloat() ?: 0f

                // Извлекаем время начала тренировки в формате чч:мм
                val trainingStart = detailedStats.detailedClockData
                    ?.find { it.id == clockId }
                    ?.date
                    ?.let { LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")) }
                    ?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Неизвестно"


                // Добавляем в список пару с началом тренировки и продолжительностью
                totalClockDurations.add(Triple(trainingStart, formatSecondsToMinutes(meanDuration), clockId))
            }
        }

        val totalGames = (airplaneIds?.size ?: 0) + (clockIds?.size ?: 0)

        cardsList.add(
            StatisticsCardData(
                date.format(DateTimeFormatter.ofPattern("d MMMM")),
                totalGames, totalAirplaneDurations, totalClockDurations
            )
        )
    }
    companion object {
        private const val SECONDS_IN_MINUTE = 60
    }
}
