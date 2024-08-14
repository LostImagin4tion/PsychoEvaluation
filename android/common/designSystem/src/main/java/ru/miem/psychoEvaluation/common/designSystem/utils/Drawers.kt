package ru.miem.psychoEvaluation.common.designSystem.utils

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp

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