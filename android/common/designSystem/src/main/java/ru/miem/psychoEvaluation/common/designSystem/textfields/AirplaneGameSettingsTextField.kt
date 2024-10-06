package ru.miem.psychoEvaluation.common.designSystem.textfields

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.theme.airplaneGamePrimary

@Composable
fun AirplaneGameSettingsTextField(
    value: TextFieldValue,
    isError: Boolean,
    onValueChange: (TextFieldValue) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = imeAction),
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,
            cursorColor = Color.White,
            errorTextColor = Color.White,
            errorCursorColor = Color.Red,
            focusedBorderColor = airplaneGamePrimary,
            unfocusedBorderColor = airplaneGamePrimary,
            errorBorderColor = Color.Red,
            focusedContainerColor = airplaneGamePrimary,
            unfocusedContainerColor = airplaneGamePrimary,
            errorContainerColor = airplaneGamePrimary,
        ),
        modifier = modifier.width(160.dp)
    )
}
