package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.buttons

import androidx.annotation.FloatRange
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

fun DrawScope.drawRing(
    color: Color,
    diameter: Dp,
    @FloatRange(from = 0.0, 1.0)
    ringFraction: Float = .1f,
    offset: Offset = Offset.Zero
) {
    val path = Path().apply {
        val size = diameter.toPx()
        addOval(Rect(0f, 0f, size, size))
        op(
            path1 = this,
            path2 = Path().apply {
                addOval(
                    Rect(0f, 0f, size * (1 - ringFraction), size * (1 - ringFraction))
                )
                translate(Offset(size * ringFraction / 2, size * ringFraction / 2))
            },
            operation = PathOperation.Difference
        )
        if (offset != Offset.Zero) {
            translate(offset.copy(offset.x - size / 2, offset.y - size / 2))
        }
    }
    drawPath(path, color)
}
