package ru.miem.psychoEvaluation.common.designSystem.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.Scroll

@Composable
fun SingleLineChart(
    modelProducer: CartesianChartModelProducer,
    chartMarker: Marker? = null,
    modifier: Modifier = Modifier,
) {
    val lineColorInt = 0xffa485e0.toInt()
    val linesSpecs = listOf(
        rememberLineSpec(
            shader = DynamicShaders.color(Color(lineColorInt)),
            point = ShapeComponent(shape = Shapes.pillShape, color = lineColorInt),
            pointSize = 12.dp
        ),
    )

    val chartLayers = arrayOf(
        rememberLineCartesianLayer(
            lines = linesSpecs
        ),
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            *chartLayers,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis()
        ),
        scrollState = rememberVicoScrollState(
            autoScroll = Scroll.Absolute.End,
            autoScrollCondition = AutoScrollCondition.OnModelSizeIncreased,
        ),
        modelProducer = modelProducer,
        marker = chartMarker,
        horizontalLayout = HorizontalLayout.fullWidth(),
        modifier = modifier,
    )
}
