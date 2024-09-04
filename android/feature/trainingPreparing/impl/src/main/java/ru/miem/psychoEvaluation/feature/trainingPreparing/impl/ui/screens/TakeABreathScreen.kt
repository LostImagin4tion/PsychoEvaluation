package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.LargeHeadlineText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.R

@Composable
fun BoxScope.TakeABreathScreen(
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .screenPaddings()
            .align(Alignment.TopCenter)
            .padding(top = 100.dp)
    ) {
        TitleText(
            textRes = R.string.gymnastics_title_text,
            textAlign = TextAlign.Center,
            isLarge = false
        )

        Spacer(modifier = Modifier.height(180.dp))

        Image(
            painter = painterResource(R.drawable.nose),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
                .padding(30.dp)
                .size(120.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        LargeHeadlineText(
            text = stringResource(R.string.take_a_breath_text),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp,
            strokeCap = StrokeCap.Round,
            modifier = Modifier.size(40.dp),
        )
    }
}
