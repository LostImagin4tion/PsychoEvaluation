package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import androidx.compose.runtime.MutableState
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.columnSeries
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ChartUpdate(
    val modelProducer: CartesianChartModelProducer,
    val labelListKey: ExtraStore.Key<Map<Int, LocalDate>>
) {
    private lateinit var xToDates: Map<Int, LocalDate,>

    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d")

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    private fun formater(x: Float, chartValues: ChartValues, ax: AxisPosition.Vertical?): CharSequence {
        return (
            chartValues.model.extraStore[labelListKey][x.toInt()]
                ?: LocalDate.ofEpochDay(x.toLong())
            ).format(dateTimeFormatter)
    }

    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom>(::formater)

    suspend fun update(datesList: MutableMap<LocalDate, Int>) {
        xToDates = datesList.keys.associateBy { it.toEpochDay().toInt() }
        modelProducer.runTransaction {
            columnSeries { series(xToDates.keys, datesList.values) }
            updateExtras { it[labelListKey] = xToDates }
        }
    }
    private fun getDatesValuesList(
        data1: LocalDate?,
        data2: LocalDate?,
        datesList: MutableMap<LocalDate, Int>
    ): MutableMap<LocalDate, Int> {
        var addedDate: LocalDate
        if (data1 != null && data2 == null) {
            addedDate = LocalDate.parse(data1.toString())
            getValue(addedDate).let { it1 -> datesList.put(addedDate, it1) }
        } else if (data1 != null && data2 != null) {
            for (i in data1..data2 step 1) {
                addedDate = LocalDate.parse(i.toString())
                getValue(addedDate).let { datesList.put(addedDate, it) }
            }
        }
        return datesList
    }

    suspend fun onUpdateChart(data1: MutableState<Date?>?, data2: MutableState<Date?>?, chart: ChartUpdate) {
        var datesList = mutableMapOf<LocalDate, Int>()
        var date: String? = ""
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        if (data1 == null && data2 == null) {
            date = LocalDateTime.now().format(format)
            datesList = getDatesValuesList(LocalDate.parse(date.toString()), null, datesList)
            chart.update(datesList)
        } else if (data1 != null && data2?.value == null) {
            val date1 = LocalDate.parse(parsedDate(data1))
            datesList = getDatesValuesList(date1, null, datesList)
            chart.update(datesList)
        } else if (data1 != null && data2 != null) {
            val date1 = LocalDate.parse(parsedDate(data1))
            val date2 = LocalDate.parse(parsedDate(data2))
            datesList = getDatesValuesList(date1, date2, datesList)
            chart.update(datesList)
        }
    }
    fun getValue(data: LocalDate): Int {
        return data.dayOfMonth
    }

    private fun parsedDate(notParsedDate: MutableState<Date?>): String? {
        return notParsedDate.value?.getParsedDate("yyyy-MM-dd")
    }
}
