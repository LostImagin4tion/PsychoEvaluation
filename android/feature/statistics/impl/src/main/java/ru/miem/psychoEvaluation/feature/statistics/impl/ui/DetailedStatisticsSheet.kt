package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.patrykandpatrick.vico.core.model.ExtraStore
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.legend.rememberVerticalLegend
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.columnSeries
import com.patrykandpatrick.vico.core.model.lineSeries
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.SubtitleText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartBottomSheet
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartBottomSheetBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.feature.statistics.impl.R
import ru.miem.psychoEvaluation.feature.statistics.impl.StatisticsScreenImpl.Companion.HALF_PERCENT
import ru.miem.psychoEvaluation.feature.statistics.impl.state.DetailedStatisticsScreenState
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetailedStatisticsSheet(
    detailedStatisticsScreenState: DetailedStatisticsScreenState,
) {
    when (detailedStatisticsScreenState) {
        is DetailedStatisticsScreenState.DetailedAirplaneData -> {
            AirplaneSheet(
                detailedStatisticsScreenState.detailedAirplaneStatisticsState
            )
        }
        is DetailedStatisticsScreenState.DetailedClocksData -> {
            ClockSheet(
                detailedStatisticsScreenState.detailedClockStatisticsState
            )
        }
        else -> {}
    }
}

@Composable
fun AirplaneSheet(
    airplaneStatistics: DetailedAirplaneStatisticsState,
) {
    if (airplaneStatistics == DetailedAirplaneStatisticsState.Error){
        ErrorSheet()
    }
    else {
        val statistics = (airplaneStatistics as DetailedAirplaneStatisticsState.Success)
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SubtitleText(
                "${stringResource(R.string.concentration_title)} ${formatDateString(statistics.date)}",
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            LineChartVico(
                modifier = Modifier
                    .fillMaxWidth(),
                data = statistics.gsr.map{ it.toFloat() } + 1.2f
            )

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            LabelText(
                "${statistics.timePercentInLimits}% ${stringResource(R.string.percent_limits)}",
                isMedium = true
            )

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {

                    Row{
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.game_durartion),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                formatMillisecondsToMMSS(statistics.duration),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                    Row{
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.normal_gsr),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                formatMillisecondsToMMSS(statistics.timeInLimits),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Row{
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.bounds_value),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                "${statistics.gsrLowerLimit} ${statistics.gsrUpperLimit}",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Row{
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.gsr_up_normal),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                formatMillisecondsToMMSS(statistics.timeAboveUpperLimit),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                    Row{
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.gsr_under_normal),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                formatMillisecondsToMMSS(statistics.timeUnderLowerLimit),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Row{
                            Image(
                                painter = painterResource(R.drawable.check_mark),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                            Column {
                                BodyText(
                                    stringResource(R.string.exit_normal),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                BodyText(
                                    statistics.amountOfCrossingLimits.toString(),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                        }
                    }

                }
            }

        }
    }
}

@SuppressLint("DefaultLocale")
fun formatMillisecondsToMMSS(milliseconds: Int): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}


@Composable
fun ClockSheet(
    clockStatisticsState: DetailedClockStatisticsState,
) {
    if (clockStatisticsState == DetailedClockStatisticsState.Error){
        ErrorSheet()
    }
    else {
        val statistics = (clockStatisticsState as DetailedClockStatisticsState.Success)
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SubtitleText(
                "${stringResource(R.string.alertness_title)} ${formatDateString(statistics.date)}",
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            LineChartVico(
                modifier = Modifier
                    .fillMaxWidth(),
                data = statistics.gsr.map { it.toFloat() }
            )

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            LabelText(
                "${statistics.score}/${statistics.score+3} ${stringResource(R.string.right_answers)}",
                isMedium = true
            )

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {

                    Row {
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.game_durartion),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                formatMillisecondsToMMSS(statistics.duration),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))


                    Row {
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                stringResource(R.string.average_reaction_speed),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            BodyText(
                                formatMillisecondsToMMSSMS(statistics.meanReactionSpeed),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                Column(modifier = Modifier.weight(1f)) {

                    Row {
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                statistics.vigilanceRate,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                    Row {
                        Image(
                            painter = painterResource(R.drawable.check_mark),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
                        Column {
                            BodyText(
                                statistics.concentrationRate,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }

                }
            }
        }
    }
}

fun formatMillisecondsToMMSSMS(milliseconds: Float): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    val millis = milliseconds % 1000
    return String.format("%02d:%02d:%02d", minutes.toInt(), seconds.toInt(), millis.toInt())
}


@Composable
private fun LineChartVico(
    modifier: Modifier = Modifier,
    data: List<Float>
) {
//    val xAxisLabels = generateXAxisLabels(data.size)
//    val labelListKey = ExtraStore.Key<List<String>>()
    val chartEntryModelProducer = remember { CartesianChartModelProducer.build() }
//
//    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues, _ ->
//        val duration = Duration.ofSeconds(value.toLong())
//
//        val minutes = duration.toMinutes()
//        val seconds = duration.seconds % 60
//        String.format("")
//    }

//    LaunchedEffect(data) {
//        chartEntryModelProducer.runTransaction {
//
//            lineSeries { series(data.toList()) }
//            updateExtras { it[labelListKey] = xAxisLabels }
//
//        }
//    }

    LaunchedEffect(data) {
        data.let {
            chartEntryModelProducer.runTransaction {
                lineSeries {
                    series(y = it)
                }
            }
        }
    }

    val lineSpec = LineCartesianLayer.LineSpec(
        shader = DynamicShaders.color(
            psychoChartBottomSheet
        ),
        backgroundShader =  DynamicShaders.verticalGradient(
            arrayOf(
                psychoChartBottomSheetBackground,
                MaterialTheme.colorScheme.background),
            ),
        thicknessDp = 4f
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lines = listOf(lineSpec)
            ),
            legend = rememberVerticalLegend(
                items = listOf(
                    rememberLegendItem(
                        icon = ShapeComponent(
                            shape = Shapes.roundedCornerShape(
                                HALF_PERCENT, HALF_PERCENT,
                                HALF_PERCENT, HALF_PERCENT
                            ),
                            dynamicShader = DynamicShaders.color(
                                psychoChartBottomSheet
                            )
                        ),
                        label = TextComponent.build {
                            color = MaterialTheme.colorScheme.onSurface.toArgb()
                        },
                        labelText = stringResource(id = R.string.gsr_value)
                    )

            ),
                iconSize = 12.dp,
                iconPadding = 10.dp,
                spacing = 4.dp,),
            startAxis = rememberStartAxis(),
//            bottomAxis = rememberBottomAxis(
//                valueFormatter = bottomAxisValueFormatter
//            )
        ),
        chartEntryModelProducer
    )
}

@Composable
fun ErrorSheet(
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TitleText(R.string.no_data, color = MaterialTheme.colorScheme.onSurface, isLarge = true)
        Spacer(modifier = Modifier.height(Dimensions.commonSpacing*20))
    }
}

fun formatDateString(inputDate: String): String {
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)

    val date = inputFormat.parse(inputDate)

    val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

    val outputFormat = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("ru", "RU"))

    return localDateTime.format(outputFormat)
}

//fun generateXAxisLabels(size: Int): List<String> {
//    // Генерация временных меток в формате "мм:сс" для оси X
//    return List(size) { index ->
//        val seconds = index * 30  // Например, шаг по 30 секунд
//        val duration = Duration.ofSeconds(seconds.toLong())
//        val minutes = duration.toMinutes()
//        val remainingSeconds = duration.seconds % 60
//        String.format("%02d:%02d", minutes, remainingSeconds)
//    }
//}

//fun generateXAxisLabels(data: List<Float>, interval: Int = 30): List<String> {
//    return data.mapIndexed { index, _ ->
//        if (index % interval == 0) {
//            val minutes = index / 60
//            val seconds = index % 60
//            String.format("%d:%02d", minutes, seconds)
//        } else {
//            ""
//        }
//    }
//}