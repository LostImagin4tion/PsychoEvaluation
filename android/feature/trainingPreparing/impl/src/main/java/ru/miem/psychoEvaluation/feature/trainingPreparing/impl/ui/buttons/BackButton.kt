package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.utils.drawRing
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.R

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val buttonColor = MaterialTheme.colorScheme.onPrimary

    Icon(
        painter = painterResource(R.drawable.arrow_back),
        contentDescription = null,
        tint = buttonColor,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .drawBehind {
                drawRing(
                    color = buttonColor,
                    diameter = 36.dp,
                    ringFraction = 0.1f
                )
            }
            .padding(5.dp)
            .size(26.dp)
    )
}
