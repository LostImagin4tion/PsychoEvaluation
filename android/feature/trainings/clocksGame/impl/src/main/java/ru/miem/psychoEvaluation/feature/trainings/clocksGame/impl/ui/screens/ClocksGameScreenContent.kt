package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.R
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameInProgress
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.buttons.ActionButton
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.buttons.BackButton
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.clocks.Clocks
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.health.HealthBar

@Composable
fun ClocksGameScreenContent(
    state: ClocksGameInProgress,
    navigateToTrainingList: () -> Unit = {},
    onActionButtonClick: () -> Unit = {},
) = Column(
    horizontalAlignment = Alignment.Start,
    modifier = Modifier
        .screenPaddings(),
) {
    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        BackButton(
            modifier = Modifier.size(30.dp),
            onClick = navigateToTrainingList,
        )

        HealthBar(
            state = state,
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    TitleText(
        text = state.gameDurationString,
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Spacer(modifier = Modifier.height(32.dp))

    Clocks(
        state = state,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(32.dp))

    ActionButton(
        textRes = R.string.action_button_text,
        indicatorType = state.currentIndicatorType,
        onClick = onActionButtonClick
    )

    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

    LabelText(
        textRes = R.string.bottom_description_text,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}
