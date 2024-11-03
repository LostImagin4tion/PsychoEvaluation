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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.legend.rememberVerticalLegend
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.playmoweb.multidatepicker.MultiDatePicker
import com.playmoweb.multidatepicker.models.MultiDatePickerColors
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartSelectedBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartSelectedDayBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryContainerLight
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.feature.statistics.api.StatisticsScreen
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.OnComposeCards
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartProvider
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartUpdate
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.miem.psychoEvaluation.common.designSystem.state.StateHolder
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.DetailedStatisticsSheet

class StatisticsScreenImpl @Inject constructor() : StatisticsScreen {
    private val labelListKey = ExtraStore.Key<Map<Int, LocalDate>>()
    private var cardsList: MutableList<StatisticsCardData?> = mutableListOf(null)

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun StatisticsScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val viewModel: StatisticsScreenViewModel = viewModel()

        val screenState = viewModel.screenState.collectAsStateWithLifecycle()

        val modelProducer = remember { viewModel.chartModelProducer }

        val chart = ChartUpdate(modelProducer, labelListKey)

        var selectedTrainingValue by remember { mutableStateOf<Int?>(null) }

        val sheetState = ModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )
        val coroutineScope = rememberCoroutineScope()

        val result = viewModel.toStatResult<Unit>(screenState.value)

        when (result) {
            is SuccessResult -> { // Check if it's a SuccessResult
                StatisticsScreenContent(
                    showMessage = showMessage,
                    viewModel = viewModel,
                    chart = chart,
                    onRowClick = { gameId ->
                        coroutineScope.launch {
                            viewModel.onTrainingSelected(gameId.toString())
                            sheetState.show()
                        }
                    },
                    isStatisticsInProgress = false,
                    cardsList = screenState.value.cardsList,
                    data = Pair(screenState.value.statisticsState?.first?.airplaneData, screenState.value.statisticsState?.first?.clockData) // Assuming cardsList is part of the screen state
                )
            }
            is ErrorResult -> {
                (result as? ErrorResult<Unit>)?.message?.let {
                    showMessage(it.toString())
                    viewModel.resetState()
                    StatisticsScreenContent(
                        showMessage = showMessage,
                        viewModel = viewModel,
                        chart = chart,
                        onRowClick = { gameId ->
                            coroutineScope.launch {
                                viewModel.onTrainingSelected(gameId.toString())
                                sheetState.show()
                            }
                        },
                        isStatisticsInProgress = false,
                        cardsList = screenState.value.cardsList,
                        data = Pair(screenState.value.statisticsState?.first?.airplaneData, screenState.value.statisticsState?.first?.clockData) // Assuming cardsList is part of the screen state
                    )
                }
            }
            else -> {
                StatisticsScreenContent(
                    showMessage = showMessage,
                    viewModel = viewModel,
                    chart = chart,
                    onRowClick = { gameId ->
                        coroutineScope.launch {
                            viewModel.onTrainingSelected(gameId.toString())
                            sheetState.show()
                        }
                    },
                    isStatisticsInProgress = result is LoadingResult,
                    cardsList = screenState.value.cardsList,
                    data = Pair(screenState.value.statisticsState?.first?.airplaneData, screenState.value.statisticsState?.first?.clockData)
                )
            }
        }

        StateHolder(state = result) // Use the processed result

        LaunchedEffect(Unit) {
            viewModel.loadStatisticsData(chart, mutableStateOf(Date()), mutableStateOf(Date()))
        }

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                Column(modifier = Modifier.padding(16.dp)) {
                    selectedTrainingValue?.let { gameId ->
                        DetailedStatisticsSheet(selectedGameId = gameId, detailedAirplaneStatistics = viewModel::detailedAirplaneStatistics)
                    }
                    Button(onClick = { coroutineScope.launch { sheetState.hide() } }) {
                        Text("Close Sheet")
                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun StatisticsScreenContent(
        showMessage: (String) -> Unit,
        viewModel: StatisticsScreenViewModel,
        chart: ChartUpdate,
        onRowClick: (Int) -> Unit,
        isStatisticsInProgress: Boolean,
        cardsList: MutableList<StatisticsCardData?>,
        data: Pair<Map<String, Int>?, Map<String, Int>?>
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .screenPaddings()
                    .verticalScroll(state = rememberScrollState())
            )
            {
                val startDate: MutableState<Date?> = remember { mutableStateOf(null) }
                val endDate: MutableState<Date?> = remember { mutableStateOf(null) }

                // ===== UI SECTION =====

                Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                TitleText(
                    textRes = R.string.statistics_header,
                    isLarge = false,
                )

                Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 1))

                val snackScope = rememberCoroutineScope()

                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
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
                        )
                    )
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(DatePickerDefaults.colors().containerColor)
                            .padding(start = 12.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        SimpleTextButton(
                            textRes = R.string.snackbar_dates_picker_dismiss,
                            onClick = {
                                startDate.value = null
                                endDate.value = null
                            },
                            enabled = (startDate.value != null)
                        )
                        SimpleTextButton(
                            textRes = R.string.snackbar_dates_picker_save,
                            onClick = {
                                snackScope.launch {
                                    viewModel.loadStatisticsData(chart, startDate, endDate)
                                }
                            },
                            enabled = (startDate.value != null)
                        )
                    }

                    CartesianChartHost(
                        rememberCartesianChart(
                            rememberColumnCartesianLayer(
                                columnProvider = ChartProvider(chart,
                                    data
                                ),
                                dataLabel = TextComponent.build()
                            ),
                            startAxis = rememberStartAxis(),
                            bottomAxis = rememberBottomAxis(
                                valueFormatter = chart.bottomAxisValueFormatter
                            ),
                            legend = rememberVerticalLegend(
                                listOf(
                                    rememberLegendItem(
                                        icon = ShapeComponent(
                                            shape = Shapes.roundedCornerShape(
                                                HALF_PERCENT, HALF_PERCENT,
                                                HALF_PERCENT, HALF_PERCENT
                                            ),
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
                                            shape = Shapes.roundedCornerShape(
                                                HALF_PERCENT, HALF_PERCENT,
                                                HALF_PERCENT, HALF_PERCENT
                                            ),
                                            dynamicShader = DynamicShaders.color(psychoChartClock)
                                        ),
                                        label = TextComponent.build {
                                            color = MaterialTheme.colorScheme.onSurface.toArgb()
                                        },
                                        labelText = stringResource(id = R.string.alertness_title)
                                    )
                                ),
                                15.dp, 10.dp, 3.dp
                            )
                        ),
                        chart.modelProducer,
                    )

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))
                }
                OnComposeCards(cardsList = cardsList, onRowClick = onRowClick)
            }

            if (isStatisticsInProgress) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.TopCenter
                ){
                    Box(
                        modifier = Modifier
                            .width(75.dp)
                            .height(75.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomEnd = 16.dp,
                                    bottomStart = 16.dp
                                )
                            )
                            ,
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
    }
    companion object {
        private const val HALF_PERCENT = 50
    }
}
