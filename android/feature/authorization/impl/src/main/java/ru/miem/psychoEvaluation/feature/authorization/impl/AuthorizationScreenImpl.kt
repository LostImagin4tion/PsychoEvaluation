package ru.miem.psychoEvaluation.feature.authorization.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.logo.SimpleAppLogo
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.textfields.LoginTextField
import ru.miem.psychoEvaluation.common.designSystem.utils.isValidEmail
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import javax.inject.Inject

class AuthorizationScreenImpl @Inject constructor() : AuthorizationScreen {

    @Composable
    override fun AuthorizationScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        AuthorizationScreenContent(
            showMessage = showMessage,
            navigateToRegistration = { navigateToRoute(Routes.registration) },
            navigateToUserProfile = { navigateToRoute(Routes.userProfile) }
        )
    }

    @Composable
    private fun AuthorizationScreenContent(
        showMessage: (String) -> Unit,
        navigateToRegistration: () -> Unit = {},
        navigateToUserProfile: () -> Unit = {},
    ) = Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.screenPaddings()
    ) {
        var emailInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        var passwordInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }

        var isEmailInputError by remember { mutableStateOf(false) }
        var isPasswordInputError by remember { mutableStateOf(false) }

        val invalidDataMessage = stringResource(R.string.authorization_invalid_data_alert)

        val isContinueButtonEnabled = emailInput.text.isNotBlank() && passwordInput.text.isNotBlank()

        val onContinueButtonClick = {
            isEmailInputError = emailInput.text.isBlank() || !emailInput.text.isValidEmail()
            isPasswordInputError = passwordInput.text.isBlank()

            if (isEmailInputError || isPasswordInputError) {
                showMessage(invalidDataMessage)
            } else {
                navigateToUserProfile()
            }
        }

        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(96.dp))

        SimpleAppLogo()

        Spacer(modifier = Modifier.height(40.dp))

        TitleText(textRes = R.string.authorization_welcome_text, isLarge = false)

        Spacer(modifier = Modifier.height(20.dp))

        LoginTextField(
            value = emailInput,
            labelResource = R.string.authorization_email_field,
            isError = isEmailInputError,
            imeAction = ImeAction.Next,
            onValueChange = {
                isEmailInputError = false
                emailInput = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        LoginTextField(
            value = passwordInput,
            labelResource = R.string.authorization_password_field,
            isError = isPasswordInputError,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                isPasswordInputError = false
                passwordInput = it
            },
        )

        Spacer(modifier = Modifier.height(40.dp))

        FilledTextButton(
            isEnabled = isContinueButtonEnabled,
            textRes = R.string.authorization_continue_button,
            modifier = Modifier.fillMaxWidth(),
            onClick = onContinueButtonClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        SimpleTextButton(
            textRes = R.string.authorization_create_account_button,
            onClick = navigateToRegistration
        )
    }
}
