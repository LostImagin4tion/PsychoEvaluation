package ru.miem.psychoEvaluation.common.designSystem.screenTemplates

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.R
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.logo.SimpleAppLogo
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.textfields.LoginTextField
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoGray
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoOnGray

@Composable
fun WelcomeScreen(
    isRegistration: Boolean = false,
    emailInput: TextFieldValue,
    onEmailInputChanged: (TextFieldValue) -> Unit = {},
    passwordInput: TextFieldValue,
    onPasswordInputChanged: (TextFieldValue) -> Unit = {},
    isEmailInputError: Boolean,
    isPasswordInputError: Boolean,
    isContinueButtonEnabled: Boolean,
    onContinueButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onBackButtonClick: () -> Unit = {},
    @StringRes titleTextRes: Int,
    @StringRes emailTextRes: Int,
    @StringRes passwordTextRes: Int,
    @StringRes continueButtonTextRes: Int,
    @StringRes secondaryActionTextRes: Int,
    showMessage: (Int) -> Unit = {},
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxSize()
        .imePadding()
) {
    if (isRegistration) {
        Spacer(modifier = Modifier.height(48.dp))

        IconButton(
            onClick = onBackButtonClick,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))
    } else {
        Spacer(modifier = Modifier.height(96.dp))
    }

    SimpleAppLogo()

    Spacer(modifier = Modifier.height(40.dp))

    TitleText(textRes = titleTextRes, isLarge = false)

    Spacer(modifier = Modifier.height(20.dp))

    LoginTextField(
        value = emailInput,
        labelResource = emailTextRes,
        isError = isEmailInputError,
        imeAction = ImeAction.Next,
        onValueChange = onEmailInputChanged
    )

    Spacer(modifier = Modifier.height(10.dp))

    LoginTextField(
        value = passwordInput,
        labelResource = passwordTextRes,
        isError = isPasswordInputError,
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = onPasswordInputChanged,
    )

    Spacer(modifier = Modifier.height(40.dp))

    FilledTextButton(
        isEnabled = isContinueButtonEnabled,
        textRes = continueButtonTextRes,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            disabledContainerColor = psychoGray,
            disabledContentColor = psychoOnGray
        ),
        modifier = Modifier.fillMaxWidth(),
        onClick = onContinueButtonClick
    )

    Spacer(modifier = Modifier.height(20.dp))

    SimpleTextButton(
        textRes = secondaryActionTextRes,
        onClick = onSecondaryButtonClick
    )
}
