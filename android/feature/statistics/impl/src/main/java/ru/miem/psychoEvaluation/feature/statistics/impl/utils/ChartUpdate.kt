package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.columnSeries
import java.time.LocalDate
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.time.format.DateTimeFormatter
import java.util.Date

class ChartUpdate(
    date1: MutableState<Date?>?,
    date2: MutableState<Date?>?,
    modelProducer: CartesianChartModelProducer,
    val labelListKey: ExtraStore.Key<Map<Int, LocalDate>>
) {
    lateinit var xToDates: Map<Int, LocalDate,>
    val modelProducer = modelProducer

    val dateTimeFormatter = DateTimeFormatter.ofPattern("d")

    fun formater(x: Float, chartValues: ChartValues, ax: AxisPosition.Vertical?) : CharSequence {
//        Log.d("HUINA", "$x")
        return (chartValues.model.extraStore[labelListKey][x.toInt()] ?: LocalDate.ofEpochDay(x.toLong())).format(dateTimeFormatter)
    }

    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom>(::formater)


    suspend fun update(datesList: MutableMap<LocalDate, Int>){
        xToDates = datesList.keys.associateBy { it.toEpochDay().toInt() }
        modelProducer.runTransaction {
            columnSeries { series(xToDates.keys, datesList.values)}
            updateExtras {it[labelListKey] = xToDates }
        }
    }
}