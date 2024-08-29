package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.stopwatch

import androidx.annotation.Px
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.R
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.IndicatorType
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Stopwatch(
    stopwatchGameState: StopwatchGameState,
    modifier: Modifier = Modifier,
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier,
) {
    Image(
        painter = painterResource(R.drawable.stopwatch),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
    )

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .matchParentSize()
    ) {
        val center = with(LocalDensity.current) {
            Offset(
                x = maxWidth.toPx() / 2,
                y = (maxHeight.toPx()) / 2 + 20.dp.toPx()
            )
        }

        val indicatorResource = when (stopwatchGameState.currentIndicatorType) {
            IndicatorType.Success -> R.drawable.success_indicator
            IndicatorType.Failure -> R.drawable.failure_indicator
            IndicatorType.Undefined -> null
        }

        indicatorResource?.let { indicatorRes ->
            Image(
                painter = painterResource(indicatorRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
            )
        }

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .fillMaxWidth(),
        ) {
            drawHand(
                center = center,
                step = 6,
                length = 110.dp.toPx(),
                strokeWidth = 3.dp.toPx(),
                color = Color.Black,
                currentValue = stopwatchGameState.stopwatchTime,
            )
        }
    }
}

@Suppress("MagicNumber")
fun DrawScope.drawHand(
    center: Offset,
    step: Int,
    @Px length: Float,
    @Px strokeWidth: Float,
    color: Color,
    currentValue: Float,
) {
    val angle = Math.toRadians(
        ((currentValue * step) - 90f).toDouble()
    )
    val cosine = cos(angle).toFloat()
    val sinus = sin(angle).toFloat()
    val startX = center.x + strokeWidth / 2
    val startY = center.y
    val endX = center.x + length * cosine + strokeWidth / 2 * cosine
    val endY = center.y + length * sinus

    drawLine(
        color = color,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}
