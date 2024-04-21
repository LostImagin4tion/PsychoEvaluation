package ru.miem.psychoEvaluation.feature.registration.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.screenTemplates.WelcomeScreen
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.isValidEmail
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import javax.inject.Inject

class RegistrationScreenImpl @Inject constructor() : RegistrationScreen {

    @Composable
    override fun RegistrationScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        val navigateToAuthorization = { navController.navigate(Routes.authorization) }

        RegistrationScreenContent(
            showMessage = showMessage,
            navigateToAuthorization = navigateToAuthorization
        )
    }

    @Composable
    private fun RegistrationScreenContent(
        showMessage: (Int) -> Unit,
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

        val isContinueButtonEnabled = emailInput.text.isNotBlank() && passwordInput.text.isNotBlank()

        val onContinueButtonClick = {
            isEmailInputError = emailInput.text.isBlank() || !emailInput.text.isValidEmail()
            isPasswordInputError = passwordInput.text.isBlank()

            if (isEmailInputError || isPasswordInputError) {
                showMessage(R.string.registration_invalid_data_alert)
            }
        }

        // ===== UI SECTION =====

        WelcomeScreen(
            isRegistration = true,
            emailInput = emailInput,
            onEmailInputChanged = {
                isEmailInputError = false
                emailInput = it
            },
            passwordInput = passwordInput,
            onPasswordInputChanged = {
                isPasswordInputError = false
                passwordInput = it
            },
            isEmailInputError = isEmailInputError,
            isPasswordInputError = isPasswordInputError,
            isContinueButtonEnabled = isContinueButtonEnabled,
            onContinueButtonClick = onContinueButtonClick,
            onSecondaryButtonClick = navigateToAuthorization,
            onBackButtonClick = navigateToAuthorization,
            titleTextRes = R.string.registration_welcome_text,
            emailTextRes = R.string.registration_email_field,
            passwordTextRes = R.string.registration_password_field,
            continueButtonTextRes = R.string.registration_continue_button,
            secondaryActionTextRes = R.string.registration_create_account_button,
        )
    }
}
