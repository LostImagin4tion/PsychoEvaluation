package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.screens

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.textfields.AirplaneGameSettingsTextField
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.airplaneGamePrimary
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.R
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun AirplaneGameSettingsScreen(
    onBackClick: () -> Unit,
    onContinueButtonClick: (Double?, Double?, Duration?) -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .paint(
            painter = painterResource(R.drawable.airplane_game_settings_screen_background),
            contentScale = ContentScale.FillBounds,
        )
) {
    var upperBoundInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("default"))
    }
    var lowerBoundInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("default"))
    }
    var timeInGameInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("3"))
    }

    var isUpperBoundInputError by remember { mutableStateOf(false) }
    var isLowerBoundInputError by remember { mutableStateOf(false) }
    var isTimeInGameInputError by remember { mutableStateOf(false) }

    ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimensions.commonPadding)
            .padding(horizontal = Dimensions.primaryHorizontalPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(ru.miem.psychoEvaluation.common.designSystem.R.drawable.ic_back),
                    contentDescription = null,
                    tint = airplaneGamePrimary
                )
            }

            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

            TitleText(
                textRes = R.string.settings_header_text,
                isLarge = true,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        Image(
            painter = painterResource(R.drawable.airplane_with_clouds),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 16.dp)
                )
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_border_up),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp),
                )

                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                Column {
                    LabelText(
                        textRes = R.string.gsr_upper_limit_parameter,
                        textColor = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                    AirplaneGameSettingsTextField(
                        value = upperBoundInput,
                        isError = isUpperBoundInputError,
                        onValueChange = { upperBoundInput = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.commonPadding))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_border_down),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp),
                )

                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                Column() {
                    LabelText(
                        textRes = R.string.gsr_lower_limit_parameter,
                        textColor = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                    AirplaneGameSettingsTextField(
                        value = lowerBoundInput,
                        isError = isLowerBoundInputError,
                        onValueChange = { lowerBoundInput = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.commonPadding))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_time_in_game),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp),
                )

                Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

                Column() {
                    LabelText(
                        textRes = R.string.time_in_game_parameter,
                        textColor = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                    AirplaneGameSettingsTextField(
                        value = timeInGameInput,
                        isError = isTimeInGameInputError,
                        onValueChange = { timeInGameInput = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.commonPadding))

            LabelText(
                textRes = R.string.settings_disclaimer,
                isMedium = false,
                textColor = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimensions.commonPadding))

            FilledTextButton(
                textRes = R.string.continue_button_text,
                modifier = Modifier.width(160.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = airplaneGamePrimary,
                    contentColor = Color.White,
                ),
                onClick = {
                    val upperBoundText = upperBoundInput.text
                    val upperBound = upperBoundText.toDoubleOrNull()

                    val lowerBoundText = lowerBoundInput.text
                    val lowerBound = lowerBoundText.toDoubleOrNull()

                    val timeInGameText = timeInGameInput.text
                    val timeInGame = timeInGameText.toDoubleOrNull()?.toDuration(DurationUnit.MINUTES)

                    if (upperBound == null && upperBoundText.isNotBlank() && upperBoundText != "default") {
                        isUpperBoundInputError = true
                    }
                    if (lowerBound == null && lowerBoundText.isNotBlank() && lowerBoundText != "default") {
                        isLowerBoundInputError = true
                    }
                    if (timeInGame == null && timeInGameText.isNotBlank() && timeInGameText != "3") {
                        isTimeInGameInputError = true
                    }

                    if (!isUpperBoundInputError && !isLowerBoundInputError && !isTimeInGameInputError) {
                        onContinueButtonClick(upperBound, lowerBound, timeInGame)
                    }
                }
            )
        }
    }
}
