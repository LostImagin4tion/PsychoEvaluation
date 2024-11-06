package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.decorations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoBackgroundDecoration

@Composable
fun CircleBackgroundDecoration(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.size(280.dp),
    ) {
        drawCroppedCircle(
            color = psychoBackgroundDecoration,
            diameter = 280.dp,
        )
    }
}

@Suppress("MagicNumber")
private fun DrawScope.drawCroppedCircle(
    color: Color,
    diameter: Dp = 100.dp,
) {
    val path = Path().apply {
        val size = diameter.toPx()
        addOval(Rect(0f, 0f, size, size))
        op(
            path1 = this,
            path2 = Path().apply {
                addRect(
                    Rect(0f, 0f, size, size)
                )
                translate(Offset(size * 0.7f, 0f))
            },
            operation = PathOperation.Difference
        )
        op(
            path1 = this,
            path2 = Path().apply {
                addRect(
                    Rect(0f, 0f, size, size)
                )
                translate(Offset(0f, size * -0.7f))
            },
            operation = PathOperation.Difference
        )
        translate(Offset(size * 0.3f, size * -0.3f))
    }
    drawPath(path, color)
}
