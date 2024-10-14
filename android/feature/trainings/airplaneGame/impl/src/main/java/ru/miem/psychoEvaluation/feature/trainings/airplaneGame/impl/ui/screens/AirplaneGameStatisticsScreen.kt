package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.R
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameStatisticsState
import kotlin.math.roundToInt

@Composable
@Suppress("MagicNumber")
fun AirplaneGameStatisticsScreen(
    state: AirplaneGameStatisticsState,
    restartGame: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTrainingListScreen: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.screenPaddings()
) {
    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    Image(
        painter = painterResource(R.drawable.airplane_small),
        contentDescription = null,
        modifier = Modifier.size(width = 234.dp, height = 188.dp)
    )

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    TitleText(textRes = R.string.statistics_screen_title)

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        val successStarsCount = (state.successPercent * 5).toInt()
        for (i in 0 until successStarsCount) {
            Icon(
                painter = painterResource(R.drawable.ic_star_filled),
                contentDescription = null,
                tint = Color(0xFFFF9D40),
                modifier = Modifier.size(54.dp)
            )

            if (i != 4) {
                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
            }
        }
        val starsCount = 5 - successStarsCount
        for (i in 0 until starsCount) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = Color(0xFFFF9D40),
                modifier = Modifier.size(54.dp)
            )

            if (i != starsCount - 1) {
                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
            }
        }
    }

    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

    BodyText(
        text = stringResource(id = R.string.statistics_screen_time_in_corridor_percent_text)
            .format((state.successPercent * 100).roundToInt())
    )

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    TitleText(text = state.gameTime)

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

    LabelText(
        text = stringResource(R.string.statistics_screen_time_in_corridor_text)
            .format(state.timeInCorridor),
        modifier = Modifier.align(Alignment.Start),
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    LabelText(
        text = stringResource(R.string.statistics_screen_time_upper_corridor_text)
            .format(state.timeUpperCorridor),
        modifier = Modifier.align(Alignment.Start),
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    LabelText(
        text = stringResource(R.string.statistics_screen_time_lower_corridor_text)
            .format(state.timeLowerCorridor),
        modifier = Modifier.align(Alignment.Start),
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    LabelText(
        text = stringResource(R.string.statistics_screen_number_of_flights_from_outside_corridor)
            .format(state.numberOfFlightsOutsideCorridor),
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
        textRes = R.string.statistics_settings,
        onClick = navigateToSettings,
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    SimpleTextButton(
        textRes = R.string.statistics_exit,
        onClick = navigateToTrainingListScreen,
    )
}
