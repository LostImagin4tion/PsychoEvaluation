package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
@Suppress("MagicNumber")
fun StopwatchGameStatisticsScreen(
    navigateToGameStart: () -> Unit,
    navigateToTrainingListScreen: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.screenPaddings()
) {
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
        for (i in 0 until 5) {
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

    TitleText(text = "03:57")

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LabelText(
            text = stringResource(R.string.statistics_result_title)
                .format("7/10")
        )
    }

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LabelText(
            text = stringResource(R.string.statistics_reaction_title)
                .format("00:01"),
        )
    }

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    LabelText(
        textRes = R.string.statistics_good_vigilance,
        modifier = Modifier.align(Alignment.Start),
    )

    LabelText(
        textRes = R.string.statistics_good_reaction,
        modifier = Modifier.align(Alignment.Start),
    )

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

    FilledTextButton(
        textRes = R.string.statistics_play_again,
        modifier = Modifier.fillMaxWidth(),
        onClick = navigateToGameStart,
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    SimpleTextButton(
        textRes = R.string.statistics_exit,
        onClick = navigateToTrainingListScreen,
    )
}
