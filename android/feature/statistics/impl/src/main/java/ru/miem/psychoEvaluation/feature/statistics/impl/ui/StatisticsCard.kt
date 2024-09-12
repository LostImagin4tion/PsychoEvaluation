package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryContainerLight
import ru.miem.psychoEvaluation.feature.statistics.impl.R

@Composable
fun StatisticsCard(
    statisticsData: StatisticsCardData
) {
    val shape = RoundedCornerShape(10.dp)

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = statisticsData.modifier
            .clip(shape)
            .clickable(
                onClick = statisticsData.onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            )
            .shadow(
                elevation = 5.dp,
                shape = shape,
                clip = true
            )
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = shape
            )
            .padding(Dimensions.commonPadding)
            .fillMaxWidth()
    ) {
        Column() {
            BodyText(statisticsData.dateRes, isLarge = true)

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            BodyText("Тренировки: " + statisticsData.allValueRes)

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            Row() {
                Image(
                    painter = painterResource(R.drawable.concentration_game),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, psychoChartConcentration, CircleShape)
                )

                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                Column {
                    LabelText(text = "Концентрация:")
                    BodyText(statisticsData.concentrationTimeRes)
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            Row() {
                Image(
                    painter = painterResource(R.drawable.clock_game),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(60.dp)
                        .background(psychoPrimaryContainerLight, CircleShape)
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(2.dp, psychoChartClock, CircleShape)
                )

                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                Column {
                    LabelText(text = "Бдительность:")
                    BodyText(statisticsData.clockTimeRes)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 1))
}
