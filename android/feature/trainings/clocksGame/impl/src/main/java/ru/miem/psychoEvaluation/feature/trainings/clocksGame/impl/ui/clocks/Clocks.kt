package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.clocks

import androidx.annotation.Px
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.R
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameInProgress
import kotlin.math.cos
import kotlin.math.sin

@Suppress("MagicNumber")
@Composable
fun Clocks(
    state: ClocksGameInProgress,
    modifier: Modifier = Modifier,
) = Box(
    contentAlignment = Alignment.Center,
    modifier = modifier,
) {
    val hours = state.clocksTime.inWholeHours
    val minutes = state.clocksTime.inWholeMinutes - hours * 60
    val seconds = state.clocksTime.inWholeSeconds - minutes * 60

    Image(
        painter = painterResource(R.drawable.clocks),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
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
                y = maxHeight.toPx() / 2 + 14.dp.toPx()
            )
        }

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .fillMaxWidth(),
        ) {
            // seconds hand
            drawHand(
                center = center,
                degreesStep = 6,
                length = 90.dp.toPx(),
                strokeWidth = 3.dp.toPx(),
                color = Color.Black,
                currentValue = seconds.toFloat(),
            )

            // minutes hand
            drawHand(
                center = center,
                degreesStep = 6,
                length = 75.dp.toPx(),
                strokeWidth = 6.dp.toPx(),
                color = Color.Black,
                currentValue = minutes.toFloat(),
            )

            // hour hand
            drawHand(
                center = center,
                degreesStep = 30,
                length = 50.dp.toPx(),
                strokeWidth = 6.dp.toPx(),
                color = Color.Black,
                currentValue = hours.toFloat(),
            )
        }
    }
}

@Suppress("MagicNumber")
fun DrawScope.drawHand(
    center: Offset,
    degreesStep: Int,
    @Px length: Float,
    @Px strokeWidth: Float,
    color: Color,
    currentValue: Float,
) {
    val angle = Math.toRadians(
        ((currentValue * degreesStep) - 90f).toDouble()
    )
    val cosine = cos(angle).toFloat()
    val sinus = sin(angle).toFloat()
    val startX = center.x - strokeWidth / 2 * cosine
    val startY = center.y - strokeWidth / 2 * sinus
    val endX = center.x + (length + strokeWidth / 2) * cosine
    val endY = center.y + (length + strokeWidth / 2) * sinus

    drawLine(
        color = color,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}
