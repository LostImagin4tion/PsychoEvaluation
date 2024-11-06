package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.legend.rememberVerticalLegend
import com.patrykandpatrick.vico.core.chart.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.SubtitleText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartBottomSheet
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartBottomSheetBackground
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClocksStatisticsState
import ru.miem.psychoEvaluation.feature.statistics.impl.R
import ru.miem.psychoEvaluation.feature.statistics.impl.StatisticsScreenImpl.Companion.HALF_PERCENT
import ru.miem.psychoEvaluation.feature.statistics.impl.state.DetailedStatisticsScreenState
import java.text.SimpleDateFormat
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
                detailedStatisticsScreenState.detailedClocksStatisticsState
            )
        }
        else -> {}
    }
}

@Composable
fun AirplaneSheet(
    airplaneStatistics: DetailedAirplaneStatisticsState,
) {
    when (airplaneStatistics) {
        is DetailedAirplaneStatisticsState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SubtitleText(
                    text = "${stringResource(R.string.concentration_title)} " +
                        formatDateString(airplaneStatistics.date),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                LineChartVico(
                    modifier = Modifier
                        .fillMaxWidth(),
                    data = airplaneStatistics.gsr.map { it.toFloat() },
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                LabelText(
                    text = "${airplaneStatistics.timePercentInLimits}% ${stringResource(R.string.percent_limits)}",
                    isMedium = true,
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row {
                            Image(
                                painter = painterResource(R.drawable.check_mark),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    text = stringResource(R.string.game_durartion),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    text = formatMillisecondsToMMSS(airplaneStatistics.duration),
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    text = stringResource(R.string.normal_gsr),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    text = formatMillisecondsToMMSS(airplaneStatistics.timeInLimits),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }

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
                                    text = stringResource(R.string.bounds_value),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    text = "${airplaneStatistics.gsrLowerLimit} ${airplaneStatistics.gsrUpperLimit}",
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    text = stringResource(R.string.gsr_up_normal),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    text = formatMillisecondsToMMSS(airplaneStatistics.timeAboveUpperLimit),
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    text = stringResource(R.string.gsr_under_normal),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    text = formatMillisecondsToMMSS(airplaneStatistics.timeUnderLowerLimit),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row {
                                Image(
                                    painter = painterResource(R.drawable.check_mark),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(20.dp),
                                )

                                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                                Column {
                                    BodyText(
                                        text = stringResource(R.string.exit_normal),
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )

                                    BodyText(
                                        text = airplaneStatistics.amountOfCrossingLimits.toString(),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        DetailedAirplaneStatisticsState.Error -> ErrorSheet()
    }
}

@Suppress("MagicNumber")
fun formatMillisecondsToMMSS(milliseconds: Int): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return String.format(
        locale = Locale.getDefault(),
        format = "%02d:%02d",
        minutes,
        seconds,
    )
}

@Composable
fun ClockSheet(
    clocksStatisticsState: DetailedClocksStatisticsState,
) {
    when (clocksStatisticsState) {
        is DetailedClocksStatisticsState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                SubtitleText(
                    text = "${stringResource(R.string.alertness_title)} " +
                        formatDateString(clocksStatisticsState.date),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                LineChartVico(
                    modifier = Modifier
                        .fillMaxWidth(),
                    data = clocksStatisticsState.gsr.map { it.toFloat() },
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                LabelText(
                    text = "${clocksStatisticsState.score} ${stringResource(R.string.right_answers)}",
                    isMedium = true,
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row {
                            Image(
                                painter = painterResource(R.drawable.check_mark),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    text = stringResource(R.string.game_durartion),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    text = formatMillisecondsToMMSS(clocksStatisticsState.duration),
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    stringResource(R.string.average_reaction_speed),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )

                                BodyText(
                                    formatMillisecondsToMMSSMS(clocksStatisticsState.meanReactionSpeed),
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    clocksStatisticsState.vigilanceRate,
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    .size(20.dp),
                            )

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            Column {
                                BodyText(
                                    clocksStatisticsState.concentrationRate,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
        DetailedClocksStatisticsState.Error -> ErrorSheet()
    }
}

@Suppress("MagicNumber")
fun formatMillisecondsToMMSSMS(milliseconds: Float): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    val millis = milliseconds % 1000
    return String.format(
        locale = Locale.getDefault(),
        format = "%02d:%02d:%02d",
        minutes.toInt(),
        seconds.toInt(),
        millis.toInt(),
    )
}

@Composable
private fun LineChartVico(
    modifier: Modifier = Modifier,
    data: List<Float>
) {
    val roundedCornerShape = Shapes.roundedCornerShape(
        topLeftPercent = HALF_PERCENT,
        topRightPercent = HALF_PERCENT,
        bottomRightPercent = HALF_PERCENT,
        bottomLeftPercent = HALF_PERCENT,
    )

    val chartEntryModelProducer = remember { CartesianChartModelProducer.build() }

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
            psychoChartBottomSheet,
        ),
        backgroundShader = DynamicShaders.verticalGradient(
            arrayOf(
                psychoChartBottomSheetBackground,
                MaterialTheme.colorScheme.background,
            ),
        ),
        thicknessDp = 4f,
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lines = listOf(lineSpec),
            ),
            legend = rememberVerticalLegend(
                items = listOf(
                    rememberLegendItem(
                        icon = ShapeComponent(
                            shape = roundedCornerShape,
                            dynamicShader = DynamicShaders.color(
                                psychoChartBottomSheet,
                            )
                        ),
                        label = TextComponent.build {
                            color = MaterialTheme.colorScheme.onSurface.toArgb()
                        },
                        labelText = stringResource(id = R.string.gsr_value)
                    ),
                ),
                iconSize = 12.dp,
                iconPadding = 10.dp,
                spacing = 4.dp,
            ),
            startAxis = rememberStartAxis(),
        ),
        modelProducer = chartEntryModelProducer,
    )
}

@Composable
fun ErrorSheet() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        TitleText(
            R.string.no_data,
            color = MaterialTheme.colorScheme.onSurface,
            isLarge = true,
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}

fun formatDateString(inputDate: String): String {
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)

    val date = inputFormat.parse(inputDate)

    val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

    val outputFormat = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("ru", "RU"))

    return localDateTime.format(outputFormat)
}
