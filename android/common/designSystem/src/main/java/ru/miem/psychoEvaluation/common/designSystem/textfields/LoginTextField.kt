package ru.miem.psychoEvaluation.common.designSystem.textfields

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoPrimaryGray

/**
 * [LoginTextField] - Text Field for AuthorizationScreen
 *
 * @author Egor Danilov
 */
@Composable
fun LoginTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit = {},
    @StringRes labelResource: Int,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Done,
    isError: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val trailingIcon = @Composable {
        AnimatedVisibility(
            visible = value.text.isNotEmpty(),
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            if (visualTransformation == PasswordVisualTransformation()) {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = psychoPrimaryGray
                    )
                }
            }
        }
    }

    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        labelResource = labelResource,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            visualTransformation
        },
        trailingIcon = trailingIcon,
        imeAction = imeAction,
        isError = isError,
        placeholder = placeholder,
        modifier = modifier
    )
}
