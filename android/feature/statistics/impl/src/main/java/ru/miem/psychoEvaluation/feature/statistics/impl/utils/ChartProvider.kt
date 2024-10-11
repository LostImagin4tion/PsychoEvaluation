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
            color = Color.Black.toArgb(), thicknessDp = THICKNESS,
            shape = Shapes.roundedCornerShape(ROUND_PERCENT, ROUND_PERCENT, 0, 0),
            dynamicShader = DynamicShaders.verticalGradient(
                arrayOf(psychoChartConcentration, psychoChartClock),
                floatArrayOf(CHART_COLOR_HEIGHT_CONCENTRATION, CHART_COLOR_HEIGHT_VIGILANCE)
            )
        )
        return lineComponent
    }

    private fun getGamesValues(data: LocalDate?): Array<Int> {
        return if (data != null) {
            arrayOf(data.dayOfMonth, (RANDOM_VAL_1..RANDOM_VAL_2).random())
        } else {
            arrayOf(DATES_IN_MONTH, (RANDOM_VAL_1..RANDOM_VAL_2).random())
        }
    }

    override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore): LineComponent {
        return lineComponent
    }

    companion object {
        private const val ROUND_PERCENT = 40
        private const val THICKNESS = 10f
        private const val CHART_COLOR_HEIGHT_CONCENTRATION = 0.6f
        private const val CHART_COLOR_HEIGHT_VIGILANCE = 0.61f
        private const val RANDOM_VAL_1 = 1
        private const val RANDOM_VAL_2 = 12
        private const val DATES_IN_MONTH = 30
    }
}
