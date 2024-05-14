package ru.miem.psychoEvaluation.feature.userProfile.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions

@Suppress("MagicNumber")
@Composable
fun NeuronsTextBlock(
    modifier: Modifier = Modifier
) {
    val neuronWidth = 84.dp
    val neuronHeight = 132.dp

    val redNeuronTextBackgroundColor = Color(0xFFE01E5A)
    val blueNeuronTextBackgroundColor = Color(0xFF4285F4)

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.red_neuron),
                contentDescription = null,
                modifier = Modifier
                    .size(width = neuronWidth, height = neuronHeight)
                    .weight(0.4f)
            )

            Spacer(modifier = Modifier.weight(0.05f))

            BodyText(
                isLarge = false,
                textRes = R.string.red_neuron_text,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = redNeuronTextBackgroundColor,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(all = Dimensions.commonSpacing)
                    .weight(1.0f)
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BodyText(
                isLarge = false,
                textRes = R.string.blue_neuron_text,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = blueNeuronTextBackgroundColor,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(all = Dimensions.commonSpacing)
                    .weight(1.0f)
            )

            Spacer(modifier = Modifier.weight(0.05f))

            Image(
                painter = painterResource(id = R.drawable.blue_neuron),
                contentDescription = null,
                modifier = Modifier
                    .size(width = neuronWidth, height = neuronHeight)
                    .weight(0.4f)
            )
        }
    }
}
