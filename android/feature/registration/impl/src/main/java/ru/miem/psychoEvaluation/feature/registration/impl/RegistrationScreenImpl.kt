package ru.miem.psychoEvaluation.feature.registration.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.logo.SimpleAppLogo
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.textfields.LoginTextField
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoGray
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoOnGray
import ru.miem.psychoEvaluation.common.designSystem.utils.isValidEmail
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import javax.inject.Inject

class RegistrationScreenImpl @Inject constructor() : RegistrationScreen {

    @Composable
    override fun RegistrationScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        val navigateToAuthorization = { navController.navigate(Routes.authorization) }

        RegistrationScreenContent(
            showMessage = showMessage,
            navigateToAuthorization = navigateToAuthorization
        )
    }

    @Composable
    private fun RegistrationScreenContent(
        showMessage: (String) -> Unit,
        navigateToAuthorization: () -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.mainHorizontalPadding)
            .imePadding()
    ) {
        var emailInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        var passwordInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }

        var isEmailInputError by remember { mutableStateOf(false) }
        var isPasswordInputError by remember { mutableStateOf(false) }

        val invalidDataMessage = stringResource(R.string.registration_invalid_data_alert)

        val isContinueButtonEnabled = emailInput.text.isNotBlank() && passwordInput.text.isNotBlank()

        val onContinueButtonClick = {
            isEmailInputError = emailInput.text.isBlank() || !emailInput.text.isValidEmail()
            isPasswordInputError = passwordInput.text.isBlank()

            if (isEmailInputError || isPasswordInputError) {
                showMessage(invalidDataMessage)
            }
        }

        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(48.dp))

        IconButton(
            onClick = navigateToAuthorization,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(ru.miem.psychoEvaluation.common.designSystem.R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        SimpleAppLogo()

        Spacer(modifier = Modifier.height(40.dp))

        TitleText(textRes = R.string.registration_welcome_text, isLarge = false)

        Spacer(modifier = Modifier.height(20.dp))

        LoginTextField(
            value = emailInput,
            labelResource = R.string.registration_email_field,
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
            labelResource = R.string.registration_password_field,
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
            textRes = R.string.registration_continue_button,
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
            textRes = R.string.registration_create_account_button,
            onClick = navigateToAuthorization
        )
    }
}
