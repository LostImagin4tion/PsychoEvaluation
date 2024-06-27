package ru.miem.psychoEvaluation.common.designSystem.dialogs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import ru.miem.psychoEvaluation.common.designSystem.R
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.SubtitleText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText

@Composable
fun SystemDialog(
    headerText: String,
    descriptionText: String,
    @DrawableRes iconRes: Int,
    @StringRes confirmButtonRes: Int = R.string.system_dialog_confirm_button_default_text,
    @StringRes dismissButtonRes: Int = R.string.system_dialog_dismiss_button_default_text,
    onConfirmTapped: () -> Unit = {},
    onDismissTapped: () -> Unit = {},
) {
    AlertDialog(
        title = {
            SubtitleText(
                text = headerText,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            LabelText(
                text = descriptionText,
                textAlign = TextAlign.Start,
            )
        },
        icon = {
            Icon(
                painter = painterResource(iconRes),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null
            )
        },
        onDismissRequest = onDismissTapped,
        dismissButton = {
            SimpleTextButton(
                textRes = dismissButtonRes,
                onClick = onDismissTapped,
            )
        },
        confirmButton = {
            SimpleTextButton(
                textRes = confirmButtonRes,
                onClick = onConfirmTapped,
            )
        },
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.background,
            shape = AlertDialogDefaults.shape
        )
    )
}