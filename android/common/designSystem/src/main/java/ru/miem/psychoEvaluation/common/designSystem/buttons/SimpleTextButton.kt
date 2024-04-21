package ru.miem.psychoEvaluation.common.designSystem.buttons

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryGray

@Composable
fun SimpleTextButton(
    enabled: Boolean = true,
    @StringRes textRes: Int,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            text = stringResource(textRes),
            style = style,
            color = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                psychoPrimaryGray
            },
        )
    }
}
