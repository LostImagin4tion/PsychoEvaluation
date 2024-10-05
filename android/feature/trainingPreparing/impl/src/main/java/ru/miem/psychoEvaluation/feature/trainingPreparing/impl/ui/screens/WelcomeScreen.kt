package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.R
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.buttons.ContinueButton

@Composable
fun BoxScope.WelcomeScreen(
    onContinueButtonClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .screenPaddings()
            .align(Alignment.BottomCenter)
            .padding(bottom = 40.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.girl_meditating),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(150.dp))

        TitleText(
            textRes = R.string.training_preparing_title_text,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        ContinueButton(onContinueButtonClick)
    }
}
