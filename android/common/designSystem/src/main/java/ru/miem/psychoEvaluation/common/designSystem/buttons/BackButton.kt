package ru.miem.psychoEvaluation.common.designSystem.buttons

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.utils.CommonDrawables
import ru.miem.psychoEvaluation.common.designSystem.utils.drawRing

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit = {},
) {
    Icon(
        painter = painterResource(CommonDrawables.arrow_back),
        contentDescription = null,
        tint = color,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .drawBehind {
                drawRing(
                    color = color,
                    diameter = 30.dp,
                    ringFraction = 0.1f
                )
            }
            .padding(5.dp)
            .size(20.dp)
    )
}
