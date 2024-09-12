package ru.miem.psychoEvaluation.feature.statistics.impl

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.orlandroyd.composecalendar.util.getParsedDate
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.legend.rememberVerticalLegend
import com.patrykandpatrick.vico.core.chart.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.model.ExtraStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.NothingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.Result
import ru.miem.psychoEvaluation.common.designSystem.utils.ResultNames
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsResponseType
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.StatisticsState
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCard
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartUpdate
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class Provider(private val chart: ChartUpdate) : ColumnCartesianLayer.ColumnProvider {
    private var lc = LineComponent(color = Color.Black.toArgb())

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
//        if (percent!=null){lc = LineComponent(color=Color.Black.toArgb(), thicknessDp = 10f, shape=Shapes.roundedCornerShape(40, 40, 0, 0), dynamicShader = DynamicShaders.verticalGradient(
//            arrayOf(psychoChartConcentration, psychoChartClock), floatArrayOf(percent, percent+0.01f)))
//        }
        lc = LineComponent(color = Color.Black.toArgb(), thicknessDp = 10f,
            shape = Shapes.roundedCornerShape(40, 40, 0, 0),
            dynamicShader = DynamicShaders.verticalGradient(arrayOf(psychoChartConcentration, psychoChartClock),
                floatArrayOf(0.6f, 0.61f)))
        return lc
    }

    fun getGamesValues(data: LocalDate?): Array<Int> {
        return if (data != null) {
            arrayOf(data.dayOfMonth, (1..10).random())
        } else {
            arrayOf(30, (1..10).random())
        }
    }

    override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore): LineComponent {
        return lc
    }
}

class StatisticsScreenViewModel : ViewModel() {

    private val statisticsInteractor by diApi(StatisticsInteractorDiApi::statisticsInteractor)

    private val _statisticsState = MutableStateFlow<Result<Unit>>(NothingResult())

    val statisticsState: StateFlow<Result<Unit>> = _statisticsState

    fun common_statistics(
        apiAccessToken: String,
        startDate: String,
        endDate: String
    ) {
        viewModelScope.launch {
            _statisticsState.emit(LoadingResult())
            Timber.tag(TAG).d("Got new UI state ${ResultNames.loading}")

            statisticsInteractor.common_statistics(apiAccessToken, startDate, endDate)
                .run {
                    val uiState = this.toResult<Unit>()
                    Timber.tag(TAG).d("Got new UI state $uiState")
                    _statisticsState.emit(uiState)
                }
        }
    }

    private fun <T> StatisticsState.toResult(): Result<T> = when (this.state) {
        StatisticsResponseType.StatisticAvailable -> SuccessResult()
        StatisticsResponseType.Error -> ErrorResult()
    }

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    private fun parsedDate(notParsedDate: MutableState<Date?>): String? {
        return notParsedDate.value?.getParsedDate("yyyy-MM-dd")
    }

    fun parsedApiDate(notParsedDate: MutableState<Date?>): String? {
        return notParsedDate.value?.getParsedDate("yyyy.MM.dd")
    }

    fun getValue(data: LocalDate): Int {
        return data.dayOfMonth
    }

    private fun getDatesValuesList(data1: LocalDate?, data2: LocalDate?, datesList: MutableMap<LocalDate, Int>): MutableMap<LocalDate, Int> {
        var addedDate: LocalDate
        if (data1 != null && data2 == null) {
            addedDate = LocalDate.parse(data1.toString())
            getValue(addedDate).let { it1 -> datesList.put(addedDate, it1) }
        } else if (data1 != null && data2 != null) {
            for (i in data1..data2 step 1) {
                addedDate = LocalDate.parse(i.toString())
                getValue(addedDate).let { datesList.put(addedDate, it) }
            }
        }
        return datesList
    }

    suspend fun onUpdateChart(data1: MutableState<Date?>?, data2: MutableState<Date?>?, chart: ChartUpdate) {
        var datesList = mutableMapOf<LocalDate, Int>()
        var date: String? = ""
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        if (data1 == null && data2 == null) {
            date = LocalDateTime.now().format(format)
            datesList = getDatesValuesList(LocalDate.parse(date.toString()), null, datesList)
            chart.update(datesList)
        } else if (data1 != null && data2?.value == null) {
            val date1 = LocalDate.parse(parsedDate(data1).toString())
            datesList = getDatesValuesList(date1, null, datesList)
            chart.update(datesList)
        } else if (data1 != null && data2 != null) {
            val date1 = LocalDate.parse(parsedDate(data1).toString())
            val date2 = LocalDate.parse(parsedDate(data2).toString())
            datesList = getDatesValuesList(date1, date2, datesList)
            chart.update(datesList)
        }
    }

    @SuppressLint("ResourceType")
    fun onUpdateCards(data1: MutableState<Date?>, data2: MutableState<Date?>): MutableList<StatisticsCardData?> {
        val cardsList: MutableList<StatisticsCardData?> = mutableListOf(null)
        if (data2.value != null && data1.value != null) {
            val date1 = data1.value?.getParsedDate("yyyy-MM-dd")
            val date2 = data2.value?.getParsedDate("yyyy-MM-dd")
            for (i in LocalDate.parse(date1)..LocalDate.parse(date2) step 1) {
                cardsList.add(StatisticsCardData(i.format(DateTimeFormatter.ofPattern("d MMMM")),
                    (1..12).random(), "6:05", "4:55"))
            }
        } else if (data1.value == null) {
            cardsList.add(StatisticsCardData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM")), (1..12).random(), "6:05", "4:55"))
        } else {
            val date1 = data1.value!!.getParsedDate("d MMMM")
            cardsList.add(StatisticsCardData(date1, (1..12).random(), "6:05", "4:55"))
        }
        return cardsList
    }

    @Composable
    fun OnComposeCards(cardsList: MutableList<StatisticsCardData?>) {
        for (i in cardsList) {
            if (i != null) {
                StatisticsCard(statisticsData = i)
            }
        }
    }

    @Composable
    fun ChartDescribe(chart: ChartUpdate) {
        CartesianChartHost(
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = Provider(chart),
                    dataLabel = TextComponent.build()
//                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
//                        rememberLineComponent(
//                            color = Color(0xffff5500),
//                            thickness = 16.dp,
//                        )
//                )
                ),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = chart.bottomAxisValueFormatter
                ),
                legend = rememberVerticalLegend(
                    listOf(
                        rememberLegendItem(
                            icon = ShapeComponent(shape = Shapes.roundedCornerShape(50, 50, 50, 50),
                                dynamicShader = DynamicShaders.color(psychoChartConcentration)),
                            label = TextComponent.build {
                                color = MaterialTheme.colorScheme.onSurface.toArgb()
                            },
                            labelText = "Концентрация"
                        ),
                        rememberLegendItem(
                            icon = ShapeComponent(shape = Shapes.roundedCornerShape(50, 50, 50, 50),
                                dynamicShader = DynamicShaders.color(psychoChartClock)),
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
    }

    private companion object {
        val TAG: String = StatisticsScreenViewModel::class.java.simpleName
    }

    class DateIterator(
        val startDate: LocalDate,
        val endDateInclusive: LocalDate,
        val stepDays: Long
    ) : Iterator<LocalDate> {
        private var currentDate = startDate

        override fun hasNext() = currentDate <= endDateInclusive

        override fun next(): LocalDate {
            val next = currentDate

            currentDate = currentDate.plusDays(stepDays)

            return next
        }
    }

    class DateProgression(
        override val start: LocalDate,
        override val endInclusive: LocalDate,
        val stepDays: Long = 1
    ) :
        Iterable<LocalDate>, ClosedRange<LocalDate> {

        override fun iterator(): Iterator<LocalDate> =
            DateIterator(start, endInclusive, stepDays)

        infix fun step(days: Long) = DateProgression(start, endInclusive, days)
    }
}
