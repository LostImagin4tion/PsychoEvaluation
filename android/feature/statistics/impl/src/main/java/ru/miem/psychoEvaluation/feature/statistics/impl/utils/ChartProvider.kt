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
import java.time.format.DateTimeFormatter

class ChartProvider(private val chart: ChartUpdate, data: Pair<Map<String, Int>?, Map<String, Int>?>) :
    ColumnCartesianLayer.ColumnProvider {
    private var lineComponent = LineComponent(color = Color.Black.toArgb())
    private val airplaneData = data.first
    private val clockData = data.second
    override fun getColumn(
        entry: ColumnCartesianLayerModel.Entry,
        seriesIndex: Int,
        extraStore: ExtraStore
    ): LineComponent {
        val x = entry.x.toInt()
        val currentDate = extraStore[chart.labelListKey][x]?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        val airplaneValue = airplaneData?.get(currentDate.toString()) ?: 0
        val clockValue = clockData?.get(currentDate.toString()) ?: 0

        val percent = if (airplaneValue + clockValue != 0) {
            airplaneValue.toFloat() / (airplaneValue + clockValue).toFloat()
        } else {
            0f // Обработка деления на ноль
        }
        lineComponent = LineComponent(
            color = Color.Black.toArgb(), thicknessDp = THICKNESS,
            shape = Shapes.roundedCornerShape(ROUND_PERCENT, ROUND_PERCENT, 0, 0),
            dynamicShader = DynamicShaders.verticalGradient(
                arrayOf(psychoChartConcentration, psychoChartClock),
                floatArrayOf(percent, percent + MINIMUM_PROCENT)
            )
        )
        return lineComponent
    }

    override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore): LineComponent {
        return lineComponent
    }

    companion object {
        private const val ROUND_PERCENT = 40
        private const val THICKNESS = 10f
        private const val MINIMUM_PROCENT = 0.01f
    }
}
