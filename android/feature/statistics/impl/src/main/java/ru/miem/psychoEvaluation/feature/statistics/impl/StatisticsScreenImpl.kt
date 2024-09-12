package ru.miem.psychoEvaluation.feature.statistics.impl

import android.annotation.SuppressLint
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.playmoweb.multidatepicker.MultiDatePicker
import com.playmoweb.multidatepicker.models.MultiDatePickerColors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartSelectedBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartSelectedDayBackground
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryContainerLight
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.statistics.api.StatisticsScreen
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import ru.miem.psychoEvaluation.feature.statistics.impl.utils.ChartUpdate
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

class StatisticsScreenImpl @Inject constructor() : StatisticsScreen {
    private val labelListKey = ExtraStore.Key<Map<Int, LocalDate>>()
    var cardsList: MutableList<StatisticsCardData?> = mutableListOf(null)

    @SuppressLint("UnrememberedMutableState")
    @Composable
    override fun StatisticsScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val viewModel: StatisticsScreenViewModel = viewModel()

        val statisticsState = viewModel.statisticsState.collectAsState()

        when (statisticsState.value) {
            is ErrorResult -> (statisticsState.value as? ErrorResult<Unit>)
                ?.message
                ?.let { showMessage(context.getString(it)) }
            else -> {}
        }
        StatisticsScreenContent(
            showMessage = showMessage,
            navigateToSettings = { navigateToRoute(Routes.settings) }
        )
    }

    @SuppressLint("UnrememberedMutableState", "ResourceType")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun StatisticsScreenContent(
        showMessage: (String) -> Unit,
        navigateToSettings: () -> Unit = {},
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .screenPaddings()
            .verticalScroll(state = rememberScrollState())
    ) {
        val viewModel: StatisticsScreenViewModel = viewModel()

        val chartModelProducer = CartesianChartModelProducer.build()
        val modelProducer = remember { chartModelProducer }

        val startDate: MutableState<Date?> = remember { mutableStateOf(null) }
        val endDate: MutableState<Date?> = remember { mutableStateOf(null) }

        val chart = ChartUpdate(modelProducer, labelListKey)
        val dataStore by diApi(DataStorageDiApi::dataStorage)

        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(
            textRes = R.string.statistics_header,
            isLarge = false,
        )

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 1))

        val snackState = remember { SnackbarHostState() }
        val snackScope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
            BodyText(textRes = R.string.title_dates_picker, isLarge = false)

            MultiDatePicker(
                startDate = startDate,
                endDate = endDate,
                colors = MultiDatePickerColors(cardColor = MaterialTheme.colorScheme.background,
                    dayNumberColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disableDayColor = MaterialTheme.colorScheme.surfaceDim,
                    iconColor = psychoChartSelectedDayBackground,
                    monthColor = psychoChartSelectedDayBackground,
                    selectedDayBackgroundColor = psychoChartSelectedBackground,
                    selectedDayNumberColor = psychoPrimaryContainerLight,
                    weekDayColor = MaterialTheme.colorScheme.onPrimary,
                    selectedIndicatorColor = psychoChartSelectedDayBackground)
            )
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(DatePickerDefaults.colors().containerColor)
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            )
            {
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
                            cardsList = viewModel.onUpdateCards(startDate, endDate)
                            val apiAccessToken =
                                dataStore[DataStorageKeys.apiAccessToken].first()
                                    .takeIf { it.isNotBlank() }
                            if (endDate.value != null) {
                                viewModel.common_statistics(
                                    apiAccessToken = apiAccessToken.toString(),
                                    viewModel.parsedApiDate(startDate)!!,
                                    viewModel.parsedApiDate(endDate)!!
                                )
                            }
                            else {
                                viewModel.common_statistics(
                                    apiAccessToken = apiAccessToken.toString(),
                                    viewModel.parsedApiDate(startDate)!!,
                                    viewModel.parsedApiDate(startDate)!!
                                )
                            }
                            viewModel.onUpdateChart(startDate, endDate, chart)
                        }
                    },
                    enabled = (startDate.value != null)
                )
            }

            LaunchedEffect(Unit) {
                cardsList = viewModel.onUpdateCards(mutableStateOf(null), mutableStateOf(null))
                viewModel.onUpdateChart(null, null, chart)
            }

            viewModel.ChartDescribe(chart = chart)

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            viewModel.OnComposeCards(cardsList)
        }
    }

    operator fun LocalDate.rangeTo(other: LocalDate) =
        StatisticsScreenViewModel.DateProgression(this, other)

    private companion object {
        val TAG: String = StatisticsScreenImpl::class.java.simpleName
    }
}
