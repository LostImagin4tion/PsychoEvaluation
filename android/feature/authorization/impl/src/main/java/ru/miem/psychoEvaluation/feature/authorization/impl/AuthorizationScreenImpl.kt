package ru.miem.psychoEvaluation.feature.authorization.impl

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
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import javax.inject.Inject

class AuthorizationScreenImpl @Inject constructor() : AuthorizationScreen {

    @Composable
    override fun AuthorizationScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        val navigateToRegistration = { navController.navigate(Routes.registration) }
        val navigateToUserProfile = { navController.navigate(Routes.userProfile) }

        AuthorizationScreenContent(
            showMessage = showMessage,
            navigateToRegistration = navigateToRegistration,
            navigateToUserProfile = navigateToUserProfile
        )
    }

    @Composable
    private fun AuthorizationScreenContent(
        showMessage: (Int) -> Unit,
        navigateToRegistration: () -> Unit = {},
        navigateToUserProfile: () -> Unit = {},
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
                showMessage(R.string.authorization_invalid_data_alert)
            } else {
                navigateToUserProfile()
            }
        }

        // ===== UI SECTION =====

        WelcomeScreen(
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
            onSecondaryButtonClick = navigateToRegistration,
            titleTextRes = R.string.authorization_welcome_text,
            emailTextRes = R.string.authorization_email_field,
            passwordTextRes = R.string.authorization_password_field,
            continueButtonTextRes = R.string.authorization_continue_button,
            secondaryActionTextRes = R.string.authorization_create_account_button,
        )
    }
}
