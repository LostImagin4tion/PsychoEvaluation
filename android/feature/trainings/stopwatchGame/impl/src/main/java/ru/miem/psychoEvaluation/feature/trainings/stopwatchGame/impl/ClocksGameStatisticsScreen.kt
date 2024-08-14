package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions

@Composable
fun StopwatchGameStatisticsScreen(

) = Column(
    horizontalAlignment = Alignment.Start,
    modifier = Modifier
        .screenPaddings()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.clocks_small),
            contentDescription = null
        )

        Image(
            painter = painterResource(R.drawable.confetti_background),
            contentDescription = null
        )
    }

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    TitleText(textRes = R.string.statistics_screen_title)
    
    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 0 until 5) {
            Icon(
                painter = painterResource(R.drawable.star_icon),
                contentDescription = null,
                tint = Color(0xFFFF9D40)
            )

            if (i != 4) {
                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))
            }
        }
    }

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    TitleText(
        textRes = R.string.statistics_screen_title,
        isLarge = false,
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    TitleText(text = "03:57")

    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LabelText(textRes = R.string.statistics_result_title)
    }

    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LabelText(textRes = R.string.statistics_reaction_title)
    }

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    LabelText(
        textRes = R.string.statistics_good_vigilance,
        isMedium = false,
    )

    LabelText(
        textRes = R.string.statistics_good_reaction,
        isMedium = false,
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    FilledTextButton(
        textRes = R.string.statistics_play_again,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(Dimensions.commonPadding))

    SimpleTextButton(
        textRes = R.string.statistics_exit,
    )
}