package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.ui.screens

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.soywiz.korge.android.KorgeAndroidView
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.designSystem.charts.SingleLineChart
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.AirplaneGameScreenImpl
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.GameModule
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameInProgressState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import timber.log.Timber
import java.util.Date
import kotlin.time.Duration

@Composable
fun AirplaneGameInProgressScreen(
    showMessage: (String) -> Unit,
    dataFlow: StateFlow<SensorData>,
    modelProducer: CartesianChartModelProducer,
    state: AirplaneGameInProgressState,
    onGameEnded: (Duration, Date, Duration, Duration, Duration, Int) -> Unit,
    onStartButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onStatisticsButtonClick: (Duration, Duration, Duration, Duration, Int) -> Unit,
    onExitButtonClick: () -> Unit,
    increaseGameDifficulty: () -> Unit,
    decreaseGameDifficulty: () -> Unit,
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    AndroidView(
        factory = { context ->
            val displayMetrics = context.resources.displayMetrics
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            Timber.tag(AirplaneGameScreenImpl.TAG)
                .d("Setting width $width and height $height")

            KorgeAndroidView(context).apply {
                loadModule(
                    GameModule(
                        screenWidth = width,
                        screenHeight = height,
                        context = context,
                        dataFlow = dataFlow,
                        maxGameTime = state.maxGameTime,
                        onGameEnded = onGameEnded,
                        onStartButtonClick = onStartButtonClick,
                        onSettingsButtonClick = onSettingsButtonClick,
                        onStatisticsButtonClick = onStatisticsButtonClick,
                        onExitButtonClick = onExitButtonClick,
                        increaseGameDifficulty = increaseGameDifficulty,
                        decreaseGameDifficulty = decreaseGameDifficulty,
                    )
                )
            }
        }
    )

    SingleLineChart(
        modelProducer = modelProducer,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 8.dp)
            )
            .size(width = 200.dp, height = 150.dp)
            .padding(8.dp)
    )
}
