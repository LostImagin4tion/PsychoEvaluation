package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText

@Composable
fun ActionButton(
    @StringRes textRes: Int,
    onClick: () -> Unit = {},
) {
    val backgroundShape = RoundedCornerShape(percent = 30)

    Button(
        shape = backgroundShape,
        border = BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
        ),
        contentPadding = PaddingValues(vertical = 36.dp),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        TitleText(
            isLarge = false,
            textRes = textRes,
            textAlign = TextAlign.Center,
            color = Color.Black,
        )
    }
}
