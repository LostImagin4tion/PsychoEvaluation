package ru.miem.psychoEvaluation.designSystem.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * [FilledTextButton] - basic Material You filled button
 * Contains Text and optional Icon
 *
 * @author Egor Danilov
 */
@Composable
fun FilledTextButton(
    onClick: () -> Unit = {},
    colors: ButtonColors,
    modifier: Modifier,
    @StringRes textRes: Int,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        contentPadding = PaddingValues(vertical = 15.dp),
        colors = colors,
        modifier = modifier
    ) {
        Text(
            text = stringResource(textRes),
            style = textStyle,
        )
    }
}
