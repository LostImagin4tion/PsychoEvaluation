package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.StatisticsInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedStatisticsState
import ru.miem.psychoEvaluation.feature.statistics.impl.StatisticsScreenViewModel
import ru.miem.psychoEvaluation.feature.statistics.impl.state.StatisticsCardData
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class StatisticsCardsCreator(
    private val statisticsInteractor: StatisticsInteractor,
) {
    suspend fun createCards(
        startDate: Date?,
        endDate: Date?,
    ): ImmutableList<StatisticsCardData> {
        val cardsList = when {
            endDate != null && startDate != null -> {
                val cardsList = mutableListOf<StatisticsCardData>()

                val startFormattedDate = startDate.getParsedDate("yyyy-MM-dd")
                    .let { LocalDate.parse(it) }

                val endFormattedDate = endDate.getParsedDate("yyyy-MM-dd")
                    .let { LocalDate.parse(it) }

                for (date in startFormattedDate..endFormattedDate step 1) {
                    val dateString = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                    when (val detailedStats = detailedStatistics(dateString)) {
                        is DetailedStatisticsState.Success -> {
                            val airplaneIds = detailedStats.detailedAirplaneData.map { it.id }
                            val clockIds = detailedStats.detailedClockData.map { it.id }

                            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
                            Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

                            val card = getStatisticsCard(date, airplaneIds, clockIds, detailedStats)

                            cardsList.add(card)
                        }
                        else -> {}
                    }
                }

                cardsList
            }
            startDate == null -> {
                val dateString = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                when (val detailedStats = detailedStatistics(dateString)) {
                    is DetailedStatisticsState.Success -> {
                        val airplaneIds = detailedStats.detailedAirplaneData.map { it.id }
                        val clockIds = detailedStats.detailedClockData.map { it.id }

                        Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
                        Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

                        val card = getStatisticsCard(
                            LocalDateTime.now().toLocalDate(),
                            airplaneIds,
                            clockIds,
                            detailedStats
                        )

                        listOf(card)
                    }
                    else -> emptyList()
                }
            }
            else -> {
                val dateString = startDate.getParsedDate("yyyy.MM.dd")

                when (val detailedStats = detailedStatistics(dateString)) {
                    is DetailedStatisticsState.Success -> {
                        val dateString1 = startDate.getParsedDate("yyyy-MM-dd")

                        val airplaneIds = detailedStats.detailedAirplaneData.map { it.id }
                        val clockIds = detailedStats.detailedClockData.map { it.id }

                        Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Airplane Data: $airplaneIds")
                        Timber.tag(StatisticsScreenViewModel.TAG).d("Detailed Clock Data: $clockIds")

                        val card = getStatisticsCard(
                            LocalDate.parse(dateString1),
                            airplaneIds,
                            clockIds,
                            detailedStats
                        )

                        listOf(card)
                    }
                    else -> emptyList()
                }
            }
        }

        return cardsList.toImmutableList()
    }

    private suspend fun detailedStatistics(xDate: String): DetailedStatisticsState {
        return statisticsInteractor.detailedStatistics(xDate)
    }

    private fun getStatisticsCard(
        date: LocalDate,
        airplaneIds: List<Int>,
        clockIds: List<Int>,
        detailedStats: DetailedStatisticsState.Success
    ): StatisticsCardData {
        val airplaneCardInfos = mutableListOf<StatisticsCardData.CardTrainingInfo>()
        val clocksCardInfos = mutableListOf<StatisticsCardData.CardTrainingInfo>()

        Timber.tag("STATS").d(detailedStats.toString())

        for (airplaneId in airplaneIds) {
            val meanDuration = detailedStats.detailedAirplaneData
                .find { it.id == airplaneId }
                ?.duration
                ?.toInt()
                ?: 0

            val trainingStart = detailedStats.detailedAirplaneData
                .find { it.id == airplaneId }
                ?.date
                ?.let {
                    LocalDateTime.parse(
                        it,
                        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                    )
                }
                ?.format(DateTimeFormatter.ofPattern("HH:mm"))
                ?: "Неизвестно"

            airplaneCardInfos.add(
                StatisticsCardData.CardTrainingInfo(
                    trainingStart = trainingStart,
                    trainingDuration = formatSecondsToMinutes(meanDuration),
                    trainingId = airplaneId,
                )
            )
        }

        for (clockId in clockIds) {
            val meanDuration = detailedStats.detailedClockData
                .find { it.id == clockId }
                ?.duration
                ?.toInt()
                ?: 0

            val trainingStart = detailedStats.detailedClockData
                .find { it.id == clockId }
                ?.date
                ?.let {
                    LocalDateTime.parse(
                        it,
                        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                    )
                }
                ?.format(DateTimeFormatter.ofPattern("HH:mm"))
                ?: "Неизвестно"

            clocksCardInfos.add(
                StatisticsCardData.CardTrainingInfo(
                    trainingStart = trainingStart,
                    trainingDuration = formatSecondsToMinutes(meanDuration),
                    trainingId = clockId,
                )
            )
        }

        val totalGames = airplaneIds.size + clockIds.size

        return StatisticsCardData(
            date = date.format(DateTimeFormatter.ofPattern("d MMMM")),
            totalGamesNumber = totalGames,
            airplaneCardInfos = airplaneCardInfos.toImmutableList(),
            clocksCardInfos = clocksCardInfos.toImmutableList(),
        )
    }

    private fun formatSecondsToMinutes(seconds: Int): String {
        return if (seconds != 0) {
            val minutes = seconds / SECONDS_IN_MINUTE
            val remainingSeconds = seconds % SECONDS_IN_MINUTE

            "$minutes:$remainingSeconds"
        } else {
            "0:00"
        }
    }

    companion object {
        private const val SECONDS_IN_MINUTE = 60
    }
}
