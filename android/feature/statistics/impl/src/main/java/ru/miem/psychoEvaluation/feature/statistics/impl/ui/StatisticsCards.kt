package ru.miem.psychoEvaluation.feature.statistics.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import kotlinx.collections.immutable.ImmutableList
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartClock
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoChartConcentration
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryContainerLight
import ru.miem.psychoEvaluation.feature.statistics.impl.R
import ru.miem.psychoEvaluation.feature.statistics.impl.state.StatisticsCardData

@Composable
fun StatisticsCards(
    cardsList: ImmutableList<StatisticsCardData>,
    onRowClick: (Int, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        cardsList.forEach { card ->
            StatisticsCard(statisticsData = card, onRowClick)

            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))
        }
    }
}

@Composable
private fun StatisticsCard(
    statisticsData: StatisticsCardData,
    onRowClick: (Int, String) -> Unit
) {
    val shape = remember { RoundedCornerShape(10.dp) }

    Column(
        modifier = Modifier
            .clip(shape)
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
        BodyText(statisticsData.date, isLarge = true)

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        Row {
            BodyText(R.string.trainings_title)

            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

            BodyText(statisticsData.totalGamesNumber.toString())
        }

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            statisticsData.airplaneCardInfos.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable { onRowClick(item.trainingId, "airplane") }
                ) {
                    Image(
                        painter = painterResource(R.drawable.concentration_game),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, psychoChartConcentration, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                    Column {
                        LabelText(textRes = R.string.concentration_title)

                        Row {
                            BodyText(item.trainingDuration)

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            BodyText("(${item.trainingStart})")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            statisticsData.clocksCardInfos.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable { onRowClick(item.trainingId, "clocks") }

                ) {
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
                        LabelText(textRes = R.string.alertness_title)

                        Row {
                            BodyText(item.trainingDuration)

                            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                            BodyText("(${item.trainingStart})")
                        }
                    }
                }
            }
        }
    }
}
