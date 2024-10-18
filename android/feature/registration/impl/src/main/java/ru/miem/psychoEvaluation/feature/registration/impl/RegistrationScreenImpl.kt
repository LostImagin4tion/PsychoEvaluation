package ru.miem.psychoEvaluation.feature.registration.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptionsBuilder
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.logo.SimpleAppLogo
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.state.StateHolder
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.textfields.LoginTextField
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.FullScreenLoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.LoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult
import ru.miem.psychoEvaluation.common.designSystem.utils.isValidEmail
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import javax.inject.Inject

class RegistrationScreenImpl @Inject constructor() : RegistrationScreen {

    @Composable
    override fun RegistrationScreen(
        navigateToRoute: (route: String, builder: NavOptionsBuilder.() -> Unit) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val viewModel: RegistrationViewModel = viewModel()

        val registrationState by viewModel.registrationState.collectAsStateWithLifecycle()

        registrationState.run {
            when (this) {
                is SuccessResult -> navigateToRoute(Routes.settings) {
                    popUpTo(Routes.registration) { inclusive = true }
                }
                is ErrorResult -> this.message?.let {
                    showMessage(context.getString(it))
                    viewModel.resetState()
                }
                else -> {}
            }
        }

        StateHolder(state = registrationState)

        if (registrationState !is FullScreenLoadingResult) {
            RegistrationScreenContent(
                showMessage = showMessage,
                isRegistrationInProgress = registrationState is LoadingResult,
                tryRegistration = { email, password -> viewModel.registration(email, password) },
                navigateToAuthorization = { navigateToRoute(Routes.authorization) {} },
            )
        }
    }

    @Composable
    private fun RegistrationScreenContent(
        showMessage: (String) -> Unit,
        navigateToAuthorization: () -> Unit,
        isRegistrationInProgress: Boolean,
        tryRegistration: (email: String, password: String) -> Unit = { _, _ -> },
    ) = Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.screenPaddings()
    ) {
        val uriHandler = LocalUriHandler.current

        var emailInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        var passwordInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        val acceptUserAgreementString = buildAnnotatedString {
            val notClickableText = stringResource(R.string.registration_accept_user_agreement_not_clickable_text)
            val clickableText = stringResource(R.string.registration_accept_user_agreement_clickable_text)

            append(notClickableText)

            pushStringAnnotation(tag = UserAgreementTag, annotation = UserAgreementUrl)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(clickableText)
            }
            pop()
        }
        val acceptPrivacyPolicyString = buildAnnotatedString {
            val notClickableText = stringResource(R.string.registration_accept_privacy_policy_not_clickable_text)
            val clickableText = stringResource(R.string.registration_accept_privacy_policy_clickable_text)
            append(notClickableText)

            pushStringAnnotation(tag = PrivacyPolicyTag, annotation = PrivacyPolicyUrl)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(clickableText)
            }
            pop()
        }

        var isEmailInputError by remember { mutableStateOf(false) }
        var isPasswordInputError by remember { mutableStateOf(false) }
        var isUserAgreementAccepted by remember { mutableStateOf(false) }
        var isPrivacyPolicyAccepted by remember { mutableStateOf(false) }

        val invalidDataMessage = stringResource(R.string.registration_invalid_data_alert)

        val isContinueButtonEnabled = emailInput.text.isNotBlank() &&
            passwordInput.text.isNotBlank() &&
            isUserAgreementAccepted &&
            isPrivacyPolicyAccepted

        val onContinueButtonClick = {
            val email = emailInput.text
            val password = passwordInput.text

            isEmailInputError = email.isBlank() || !email.isValidEmail()
            isPasswordInputError = password.isBlank()

            if (isEmailInputError || isPasswordInputError) {
                showMessage(invalidDataMessage)
            } else {
                tryRegistration(email, password)
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

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(Dimensions.commonPadding))

            Checkbox(
                checked = isUserAgreementAccepted,
                onCheckedChange = { isUserAgreementAccepted = !isUserAgreementAccepted },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                    checkmarkColor = Color.White,
                ),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(Dimensions.commonPadding))

            ClickableText(
                text = acceptUserAgreementString,
                style = MaterialTheme.typography.bodyMedium
                    .copy(color = MaterialTheme.colorScheme.onPrimary),
            ) { offset ->
                acceptUserAgreementString
                    .getStringAnnotations(tag = UserAgreementTag, start = offset, end = offset)
                    .firstOrNull()
                    ?.let {
                        uriHandler.openUri(it.item)
                    }
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.commonPadding))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(Dimensions.commonPadding))

            Checkbox(
                checked = isPrivacyPolicyAccepted,
                onCheckedChange = { isPrivacyPolicyAccepted = !isPrivacyPolicyAccepted },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                    checkmarkColor = Color.White,
                ),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(Dimensions.commonPadding))

            ClickableText(
                text = acceptPrivacyPolicyString,
                style = MaterialTheme.typography.bodyMedium
                    .copy(color = MaterialTheme.colorScheme.onPrimary),
            ) { offset ->
                acceptPrivacyPolicyString
                    .getStringAnnotations(tag = PrivacyPolicyTag, start = offset, end = offset)
                    .firstOrNull()
                    ?.let {
                        uriHandler.openUri(it.item)
                    }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        FilledTextButton(
            isEnabled = isContinueButtonEnabled,
            textRes = R.string.registration_continue_button,
            modifier = Modifier.fillMaxWidth(),
            onClick = onContinueButtonClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        SimpleTextButton(
            textRes = R.string.registration_create_account_button,
            onClick = navigateToAuthorization
        )

        if (isRegistrationInProgress) {
            Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round,
                modifier = Modifier.size(32.dp),
            )
        }
    }

    @Suppress("MaxLineLength")
    private companion object {
        const val UserAgreementTag = "user_agreement"
        const val PrivacyPolicyTag = "privacy_policy"

        const val UserAgreementUrl = "https://docs.google.com/document/d/18B2euEyQRe21N3gy6BOljy_OZiQs-IKMioNXDE1wrOU/edit?usp=sharing"
        const val PrivacyPolicyUrl = "https://docs.google.com/document/d/1Ew-IJou2W47RO4412q1_i3i5Usixpd9cpqnvZLyie94/edit?usp=sharing"
    }
}
