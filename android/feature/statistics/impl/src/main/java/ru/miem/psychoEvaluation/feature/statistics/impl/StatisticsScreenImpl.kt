package ru.miem.psychoEvaluation.feature.statistics.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartProvider
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartUpdate
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

class StatisticsScreenImpl @Inject constructor() : StatisticsScreen {
    private val labelListKey = ExtraStore.Key<Map<Int, LocalDate>>()
    private var cardsList: MutableList<StatisticsCardData?> = mutableListOf(null)

    @Composable
    override fun StatisticsScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val viewModel: StatisticsScreenViewModel = viewModel()

        val statisticsState = viewModel.statisticsState.collectAsState()

        val modelProducer = remember { viewModel.chartModelProducer }

        val chart = ChartUpdate(modelProducer, labelListKey)

        when (statisticsState.value) {
            is ErrorResult -> (statisticsState.value as? ErrorResult<Unit>)
                ?.message
                ?.let { showMessage(context.getString(it)) }
            else -> {}
        }

        LaunchedEffect(Unit) {
            cardsList = viewModel.cardUpdate.onUpdateCards(mutableStateOf(null), mutableStateOf(null))
            chart.onUpdateChart(null, null, chart)
        }

        StatisticsScreenContent(
            showMessage = showMessage,
            viewModel = viewModel,
            chart = chart
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun StatisticsScreenContent(
        showMessage: (String) -> Unit,
        viewModel: StatisticsScreenViewModel,
        chart: ChartUpdate
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .screenPaddings()
            .verticalScroll(state = rememberScrollState())
    ) {
        val startDate: MutableState<Date?> = remember { mutableStateOf(null) }
        val endDate: MutableState<Date?> = remember { mutableStateOf(null) }

        val percent = 50

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
                            cardsList = viewModel.cardUpdate.onUpdateCards(startDate, endDate)
                            viewModel.onAcceptClick(startDate, endDate)
                            chart.onUpdateChart(startDate, endDate, chart)
                        }
                    },
                    enabled = (startDate.value != null)
                )
            }

            CartesianChartHost(
                rememberCartesianChart(
                    rememberColumnCartesianLayer(
                        columnProvider = ChartProvider(chart),
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
                                    shape = Shapes.roundedCornerShape(percent, percent, percent, percent),
                                    dynamicShader = DynamicShaders.color(psychoChartConcentration)
                                ),
                                label = TextComponent.build {
                                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                                },
                                labelText = "Концентрация"
                            ),
                            rememberLegendItem(
                                icon = ShapeComponent(
                                    shape = Shapes.roundedCornerShape(percent, percent, percent, percent),
                                    dynamicShader = DynamicShaders.color(psychoChartClock)
                                ),
                                label = TextComponent.build {
                                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                                },
                                labelText = "Бдительность"
                            )
                        ),
                        15.dp, 10.dp, 3.dp
                    )
                ),
                chart.modelProducer,
            )

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            viewModel.cardUpdate.OnComposeCards(cardsList)
        }
    }
}
