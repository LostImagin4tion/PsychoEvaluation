package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.R

@Composable
fun ContinueButton(
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(R.drawable.arrow_right),
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
            .padding(16.dp)
            .size(32.dp)
    )
}
