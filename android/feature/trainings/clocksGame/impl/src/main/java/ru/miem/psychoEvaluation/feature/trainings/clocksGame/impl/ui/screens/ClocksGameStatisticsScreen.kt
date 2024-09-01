package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.R
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameEnded

@Composable
@Suppress("MagicNumber")
fun ClocksGameStatisticsScreen(
    state: ClocksGameEnded,
    restartGame: () -> Unit,
    navigateToTrainingListScreen: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.screenPaddings()
) {
    val vigilanceStringRes = if (state.vigilanceDelta < 0) {
        R.string.statistics_bad_vigilance
    } else {
        R.string.statistics_good_vigilance
    }

    val concentrationStringRes = if (state.concentrationDelta < 0) {
        R.string.statistics_bad_reaction
    } else {
        R.string.statistics_good_reaction
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.confetti_background),
            contentDescription = null
        )

        Image(
            painter = painterResource(R.drawable.clocks_small),
            contentDescription = null
        )
    }

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    TitleText(textRes = R.string.statistics_screen_title)

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        val starCount = (state.successPercent * 5).toInt()
        for (i in 0 until starCount) {
            Icon(
                painter = painterResource(R.drawable.star_icon),
                contentDescription = null,
                tint = Color(0xFFFF9D40),
                modifier = Modifier.size(54.dp)
            )

            if (i != 4) {
                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
            }
        }
    }

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    TitleText(text = state.gameTime)

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LabelText(
            text = stringResource(R.string.statistics_result_title)
                .format("${(state.successPercent * 10).toInt()}/10")
        )
    }

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LabelText(
            text = stringResource(R.string.statistics_reaction_title)
                .format(state.averageReactionTimeString),
        )
    }

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    LabelText(
        textRes = vigilanceStringRes,
        modifier = Modifier.align(Alignment.Start),
    )

    LabelText(
        textRes = concentrationStringRes,
        modifier = Modifier.align(Alignment.Start),
    )

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

    FilledTextButton(
        textRes = R.string.statistics_play_again,
        modifier = Modifier.fillMaxWidth(),
        onClick = restartGame,
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    SimpleTextButton(
        textRes = R.string.statistics_exit,
        onClick = navigateToTrainingListScreen,
    )
}
