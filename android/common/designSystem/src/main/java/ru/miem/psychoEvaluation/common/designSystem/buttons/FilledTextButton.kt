package ru.miem.psychoEvaluation.common.designSystem.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoGray
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoOnGray

/**
 * [FilledTextButton] - basic Material You filled button
 * Contains Text and optional Icon
 *
 * @author Egor Danilov
 */
@Composable
fun FilledTextButton(
    isEnabled: Boolean = true,
    @StringRes textRes: Int,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        disabledContainerColor = psychoGray,
        disabledContentColor = psychoOnGray
    ),
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        contentPadding = contentPadding,
        colors = colors,
        modifier = modifier
    ) {
        Text(
            text = stringResource(textRes),
            style = textStyle,
        )
    }
}
