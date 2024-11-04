package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedAirplaneStatisticsState
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.DetailedClockStatisticsState
import ru.miem.psychoEvaluation.feature.statistics.impl.R
import kotlin.reflect.KSuspendFunction1

@Composable
fun DetailedStatisticsSheet(
    detailedStatistics: Pair<DetailedAirplaneStatisticsState?, DetailedClockStatisticsState?>?
) {

    if(detailedStatistics?.first!=null){
        AirplaneSheet(detailedStatistics.first)
    }
    else{
        ClockSheet(detailedStatistics?.second)
    }

}
@Composable
fun LineChartVico(
    modifier: Modifier = Modifier,
    data: List<Float>
) {
    val chartEntryModelProducer = remember { CartesianChartModelProducer.build() }


    // Обновляем модель графика с новыми данными
    LaunchedEffect(data) {
        data.let {
            chartEntryModelProducer.runTransaction {
                lineSeries {
                    // Используем преобразованные данные из списка
                    series(y = it)
                }
            }
        }
    }
    // Конфигурируем оси и график
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        ),
        chartEntryModelProducer
    )
}
@Composable
fun AirplaneSheet(data: DetailedAirplaneStatisticsState?){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Заголовок
        BodyText(R.string.concentration_title, color = Color.Black, isLarge = true)

        // График
        LineChartVico(
            modifier = Modifier
                .fillMaxWidth(),
            data = listOf(0.2f, 0.4f, 0.8f, 0.3f, 0.6f, 0.7f, 0.9f)
        )

        // Данные
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Длительность игры (сек)")
                Text("Средняя скорость реакции")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Склонность к потере бдительности"
                )
                Text("Снижение концентрации ✓"
                )
            }
        }
    }
}

@Composable
fun ClockSheet(data: DetailedClockStatisticsState?){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Заголовок
        BodyText(R.string.alertness_title, color = Color.Black, isLarge = true)

        // График
        LineChartVico(
            modifier = Modifier
                .fillMaxWidth(),
            data = listOf(0.2f, 0.4f, 0.8f, 0.3f, 0.6f, 0.7f, 0.9f)
        )

        // Данные
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Длительность игры (сек)")
                Text("Средняя скорость реакции")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Склонность к потере бдительности"
                )
                Text("Снижение концентрации ✓"
                )
            }
        }
    }
}
