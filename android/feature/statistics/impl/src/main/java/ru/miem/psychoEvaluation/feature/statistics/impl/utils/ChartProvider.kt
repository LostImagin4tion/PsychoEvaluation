package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.chart.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.model.ExtraStore
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import java.time.LocalDate

class ChartProvider(private val chart: ChartUpdate) : ColumnCartesianLayer.ColumnProvider {
    private var lineComponent = LineComponent(color = Color.Black.toArgb())

    override fun getColumn(
        entry: ColumnCartesianLayerModel.Entry,
        seriesIndex: Int,
        extraStore: ExtraStore
    ): LineComponent {
        val roundPercent = 40
        val thickness = 10f
        val x = entry.x.toInt()
        val gameValues = getGamesValues(extraStore[chart.labelListKey][x])
        val concentrationGames = gameValues.get(1)
        val allGames = gameValues[0]
        val percent = allGames.let { concentrationGames.div(it.toFloat()).toFloat() }
//        if (percent!=null){lc = LineComponent(color=Color.Black.toArgb(), thicknessDp = 10f,
//        shape=Shapes.roundedCornerShape(40, 40, 0, 0), dynamicShader = DynamicShaders.verticalGradient(
//            arrayOf(psychoChartConcentration, psychoChartClock), floatArrayOf(percent, percent+0.01f)))
//        }
        lineComponent = LineComponent(
            color = Color.Black.toArgb(), thicknessDp = thickness,
            shape = Shapes.roundedCornerShape(roundPercent, roundPercent, 0, 0),
            dynamicShader = DynamicShaders.verticalGradient(
                arrayOf(psychoChartConcentration, psychoChartClock),
                floatArrayOf(0.6f, 0.61f)
            )
        )
        return lineComponent
    }

    private fun getGamesValues(data: LocalDate?): Array<Int> {
        val randomVal1 = 1
        val randomVal2 = 10
        val datesOfMonth = 30
        return if (data != null) {
            arrayOf(data.dayOfMonth, (randomVal1..randomVal2).random())
        } else {
            arrayOf(datesOfMonth, (randomVal1..randomVal2).random())
        }
    }

    override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore): LineComponent {
        return lineComponent
    }
}
