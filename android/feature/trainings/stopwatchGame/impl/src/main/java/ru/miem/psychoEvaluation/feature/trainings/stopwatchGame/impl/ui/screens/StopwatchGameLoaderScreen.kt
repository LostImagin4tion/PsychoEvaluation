package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.DisplayText
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameLoadingState

@Composable
fun StopwatchGameLoaderScreen(
    state: StopwatchGameLoadingState,
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.screenPaddings(),
) {
    CircularProgressIndicator(
        progress = { state.progress.toFloat() },
        modifier = Modifier.size(300.dp),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 3.dp,
        strokeCap = StrokeCap.Round,
    )

    DisplayText(
        text = state.timeBeforeStart
            .inWholeSeconds
            .plus(1)
            .toString(),
    )
}
