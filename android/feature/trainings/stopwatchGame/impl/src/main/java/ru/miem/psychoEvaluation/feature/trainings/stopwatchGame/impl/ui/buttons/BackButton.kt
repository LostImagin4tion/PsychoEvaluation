package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.R

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Icon(
    painter = painterResource(R.drawable.arrow_back_left),
    contentDescription = null,
    modifier = modifier
        .clickable { onClick() }
)