package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.columnSeries
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.CommonStatisticsState
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class ChartController(
    private val modelProducer: CartesianChartModelProducer,
) {
    val labelListKey = ExtraStore.Key<Map<Int, LocalDate>>()

    suspend fun updateChart(
        startDate: Date,
        endDate: Date,
        commonStatistics: CommonStatisticsState.Success,
    ) {
        Timber.tag("gamesData").d(commonStatistics.toString())

        val startDateParsed = LocalDate.parse(parsedDate(startDate))
        val endDateParsed = LocalDate.parse(parsedDate(endDate))

        val datesMap = getDatesValuesList(
            startDateParsed,
            endDateParsed,
            commonStatistics,
        )

        update(datesMap)
    }

    private fun getDatesValuesList(
        startDate: LocalDate,
        endDate: LocalDate,
        commonStatisticsState: CommonStatisticsState.Success,
    ): Map<LocalDate, Int> {
        val datesMap = mutableMapOf<LocalDate, Int>()

        for (i in startDate..endDate step 1) {
            val addedDate = LocalDate.parse(i.toString())

            val numberOfTrainings = getNumberOfTrainings(
                addedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                commonStatisticsState
            )
            datesMap[addedDate] = numberOfTrainings
        }

        return datesMap
    }

    private fun getNumberOfTrainings(
        data: String,
        commonStatisticsState: CommonStatisticsState.Success
    ): Int {
        val airplaneValue = commonStatisticsState.airplaneData[data] ?: 0
        val clockValue = commonStatisticsState.clockData[data] ?: 0

        return airplaneValue + clockValue
    }

    private suspend fun update(datesMap: Map<LocalDate, Int>) {
        val xToDates = datesMap.keys.associateBy { it.toEpochDay().toInt() }

        modelProducer.runTransaction {
            columnSeries { series(xToDates.keys, datesMap.values) }

            updateExtras { it[labelListKey] = xToDates }
        }
    }

    private fun parsedDate(date: Date): String {
        return date.getParsedDate("yyyy-MM-dd")
    }
}
