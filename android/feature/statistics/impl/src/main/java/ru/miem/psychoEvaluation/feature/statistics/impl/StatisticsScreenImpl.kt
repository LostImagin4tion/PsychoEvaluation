package ru.miem.psychoEvaluation.feature.statistics.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.legend.rememberVerticalLegend
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.playmoweb.multidatepicker.MultiDatePicker
import com.playmoweb.multidatepicker.models.MultiDatePickerColors
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartSelectedBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartSelectedDayBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryContainerLight
import ru.miem.psychoEvaluation.common.designSystem.utils.optionalCast
import ru.miem.psychoEvaluation.feature.statistics.api.StatisticsScreen
import ru.miem.psychoEvaluation.feature.statistics.impl.state.CommonStatisticsScreenState
import ru.miem.psychoEvaluation.feature.statistics.impl.state.DetailedStatisticsScreenState
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.DetailedStatisticsSheet
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCards
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartProvider
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

class StatisticsScreenImpl @Inject constructor() : StatisticsScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun StatisticsScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val viewModel: StatisticsScreenViewModel = viewModel()

        val commonStatisticsScreenState by viewModel.commonStatisticsScreenStateStateFlow.collectAsStateWithLifecycle()
        val detailedStatisticsScreenState by viewModel.detailedStatisticsScreenState.collectAsStateWithLifecycle()

        val startDate: MutableState<Date?> = remember { mutableStateOf(Date()) }
        val endDate: MutableState<Date?> = remember { mutableStateOf(Date()) }

        var shouldShowBottomSheet by rememberSaveable { mutableStateOf(false) }

        val sheetState = rememberModalBottomSheetState()

        val bottomSheetCoroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.requestCommonStatistics(
                startDate.value ?: Date(),
                endDate.value ?: Date(),
            )
        }

        LaunchedEffect(detailedStatisticsScreenState) {
            when (detailedStatisticsScreenState) {
                is DetailedStatisticsScreenState.Nothing -> {
                    bottomSheetCoroutineScope
                        .launch {
                            sheetState.hide()
                        }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                shouldShowBottomSheet = false
                            }
                        }
                }

                is DetailedStatisticsScreenState.Loading,
                is DetailedStatisticsScreenState.Error,
                is DetailedStatisticsScreenState.DetailedAirplaneData,
                is DetailedStatisticsScreenState.DetailedClocksData -> {
                    shouldShowBottomSheet = true
                }
            }
        }

        StatisticsScreenContent(
            showMessage = showMessage,
            startDate = startDate,
            endDate = endDate,
            screenState = commonStatisticsScreenState,
            chartModelProducer = viewModel.chartModelProducer,
            labelListKey = viewModel.labelListKey,
            bottomAxisValueFormatter = viewModel.bottomAxisValueFormatter,
            requestCommonStatistics = viewModel::requestCommonStatistics,
            onRowClick = { gameId, trainingType: String ->
                viewModel.onTrainingSelected(
                    gameId.toString(),
                    trainingType
                )
            },
        )

        if (shouldShowBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = {
                    viewModel.resetDetailedStatisticsScreenState()
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    DetailedStatisticsSheet(
                        detailedStatisticsScreenState = detailedStatisticsScreenState
                    )

                    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White,
                            ),
                            onClick = {
                                viewModel.resetDetailedStatisticsScreenState()
                            }
                        ) {
                            Text(stringResource(R.string.close_sheet))
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun StatisticsScreenContent(
        showMessage: (String) -> Unit,
        startDate: MutableState<Date?>,
        endDate: MutableState<Date?>,
        screenState: CommonStatisticsScreenState,
        chartModelProducer: CartesianChartModelProducer,
        labelListKey: ExtraStore.Key<Map<Int, LocalDate>>,
        bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>,
        requestCommonStatistics: (Date, Date) -> Unit,
        onRowClick: (Int, String) -> Unit,
    ) {
        val roundedCornerShape = Shapes.roundedCornerShape(
            topLeftPercent = HALF_PERCENT,
            topRightPercent = HALF_PERCENT,
            bottomRightPercent = HALF_PERCENT,
            bottomLeftPercent = HALF_PERCENT,
        )

        val successScreenState = screenState.optionalCast<CommonStatisticsScreenState.Success>()
        val commonStatistics = successScreenState?.commonStatisticsState

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .screenPaddings()
                    .verticalScroll(state = rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                TitleText(
                    textRes = R.string.statistics_header,
                    isLarge = false,
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                ) {
                    BodyText(textRes = R.string.title_dates_picker, isLarge = false)

                    MultiDatePicker(
                        startDate = startDate,
                        endDate = endDate,
                        colors = MultiDatePickerColors(
                            cardColor = MaterialTheme.colorScheme.background,
                            dayNumberColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disableDayColor = MaterialTheme.colorScheme.surfaceDim,
                            iconColor = psychoChartSelectedDayBackground,
                            monthColor = psychoChartSelectedDayBackground,
                            selectedDayBackgroundColor = psychoChartSelectedBackground,
                            selectedDayNumberColor = psychoPrimaryContainerLight,
                            weekDayColor = MaterialTheme.colorScheme.onPrimary,
                            selectedIndicatorColor = psychoChartSelectedDayBackground
                        ),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DatePickerDefaults.colors().containerColor)
                            .padding(start = 12.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        SimpleTextButton(
                            textRes = R.string.snackbar_dates_picker_dismiss,
                            onClick = {
                                startDate.value = null
                                endDate.value = null
                            },
                            enabled = startDate.value != null
                        )

                        SimpleTextButton(
                            textRes = R.string.snackbar_dates_picker_save,
                            onClick = {
                                if (endDate.value == null) {
                                    endDate.value = startDate.value
                                }
                                requestCommonStatistics(
                                    startDate.value ?: Date(),
                                    endDate.value ?: Date(),
                                )
                            },
                            enabled = startDate.value != null
                        )
                    }

                    if (
                        commonStatistics?.airplaneData?.isNotEmpty() == true ||
                        commonStatistics?.clockData?.isNotEmpty() == true
                    ) {
                        CartesianChartHost(
                            chart = rememberCartesianChart(
                                rememberColumnCartesianLayer(
                                    columnProvider = ChartProvider(
                                        labelListKey = labelListKey,
                                        airplaneData = commonStatistics
                                            .airplaneData,
                                        clocksData = commonStatistics
                                            .clockData,
                                    ),
                                    dataLabel = TextComponent.build()
                                ),
                                startAxis = rememberStartAxis(),
                                bottomAxis = rememberBottomAxis(
                                    valueFormatter = bottomAxisValueFormatter
                                ),
                                legend = rememberVerticalLegend(
                                    items = listOf(
                                        rememberLegendItem(
                                            icon = ShapeComponent(
                                                shape = roundedCornerShape,
                                                dynamicShader = DynamicShaders.color(
                                                    psychoChartConcentration
                                                )
                                            ),
                                            label = TextComponent.build {
                                                color = MaterialTheme.colorScheme.onSurface.toArgb()
                                            },
                                            labelText = stringResource(id = R.string.concentration_title)
                                        ),
                                        rememberLegendItem(
                                            icon = ShapeComponent(
                                                shape = roundedCornerShape,
                                                dynamicShader = DynamicShaders.color(
                                                    psychoChartClock
                                                )
                                            ),
                                            label = TextComponent.build {
                                                color = MaterialTheme.colorScheme.onSurface.toArgb()
                                            },
                                            labelText = stringResource(id = R.string.alertness_title)
                                        )
                                    ),
                                    iconSize = 16.dp,
                                    iconPadding = 10.dp,
                                    spacing = 4.dp,
                                )
                            ),
                            modelProducer = chartModelProducer
                        )
                    } else {
                        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

                        LabelText(
                            textRes = R.string.no_data,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            isLarge = true,
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))
                }

                StatisticsCards(
                    cardsList = successScreenState?.cardsList ?: persistentListOf(),
                    onRowClick = onRowClick,
                )
            }

            if (screenState is CommonStatisticsScreenState.Loading) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.Center)
                        .background(
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomEnd = 16.dp,
                                bottomStart = 16.dp
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }

    companion object {
        const val HALF_PERCENT = 50
    }
}
