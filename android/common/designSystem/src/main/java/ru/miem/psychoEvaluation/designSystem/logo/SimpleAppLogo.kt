package ru.miem.psychoEvaluation.designSystem.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.R

@Composable
fun SimpleAppLogo(
    modifier: Modifier = Modifier
) = Image(
    painter = painterResource(id = R.drawable.ic_app_logo),
    contentDescription = null,
    modifier = modifier.size(120.dp)
)
