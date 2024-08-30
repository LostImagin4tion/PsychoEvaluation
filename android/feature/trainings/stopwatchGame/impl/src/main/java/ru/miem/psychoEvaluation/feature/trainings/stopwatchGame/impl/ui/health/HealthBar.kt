package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.health

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.R
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameInProgress

@Composable
@Suppress("MagicNumber")
fun HealthBar(
    state: StopwatchGameInProgress,
    modifier: Modifier = Modifier,
) {
    val backgroundShape = RoundedCornerShape(percent = 50)
    val maxHeartsNumber = 3
    val shouldShowBrokenHeart =
        state.currentIndicatorType == StopwatchGameInProgress.IndicatorType.Failure
    val additionalBrokenHeartNumber = if (shouldShowBrokenHeart) 1 else 0
    val heartsNumber = (state.heartsNumber + additionalBrokenHeartNumber)
        .coerceAtMost(maxHeartsNumber)

    val heartSize = 26.dp
    val rowWidth = heartSize * 3 + Dimensions.commonSpacing * 2

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .background(
                shape = backgroundShape,
                color = Color(0xFFBFDDF6)
            )
            .padding(
                vertical = Dimensions.commonSpacing / 2,
                horizontal = Dimensions.commonPadding
            )
            .size(width = rowWidth, height = heartSize)
    ) {
        for (index in 0 until heartsNumber) {
            val padding = if (index == 0) 0.dp else Dimensions.commonSpacing
            val painterRes = if (index == 0 && shouldShowBrokenHeart) {
                R.drawable.broken_heart
            } else {
                R.drawable.full_heart
            }

            Image(
                painter = painterResource(painterRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = padding)
                    .size(heartSize)
            )
        }
    }
}
