package ru.miem.psychoEvaluation.feature.trainings.impl

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.designSystem.text.BodyText
import ru.miem.psychoEvaluation.designSystem.text.LabelText
import ru.miem.psychoEvaluation.designSystem.text.TitleText
import ru.miem.psychoEvaluation.designSystem.theme.Dimensions

@Composable
fun TrainingCard(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
                clip = true
            )
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(Dimensions.commonSpacing)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            LabelText(textRes = titleRes, isLarge = true)

            Spacer(modifier = Modifier.height(Dimensions.commonPadding))

            BodyText(textRes = descriptionRes)
        }

        Spacer(modifier = Modifier.weight(0.01f))

        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
    }
}