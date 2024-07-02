package ru.miem.psychoEvaluation.feature.userProfile.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoGray
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoOnGray
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen
import javax.inject.Inject

class UserProfileScreenImpl @Inject constructor() : UserProfileScreen {

    @Composable
    override fun UserProfileScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        UserProfileScreenContent(
            showMessage = showMessage,
            navigateToSettings = {
                navController.navigate(Routes.settings)
            }
        )
    }

    @Composable
    private fun UserProfileScreenContent(
        showMessage: (String) -> Unit,
        navigateToSettings: () -> Unit = {},
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding()
    ) {
        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(
            textRes = R.string.profile_header,
            isLarge = false,
        )

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

        NeuronsTextBlock()

        Spacer(modifier = Modifier.weight(1f))

        FilledTextButton(
            textRes = R.string.open_settings_button_text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.primaryHorizontalPadding),
            onClick = navigateToSettings
        )

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        FilledTextButton(
            textRes = R.string.logout_button_text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.primaryHorizontalPadding),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))
    }

    private companion object {
        val TAG: String = UserProfileScreenImpl::class.java.simpleName
    }
}
