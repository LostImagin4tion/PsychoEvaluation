package ru.miem.psychoEvaluation.feature.userProfile.impl

import android.content.Context
import android.content.Intent
import android.os.Environment
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.text.BodyText
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.SubtitleText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoGray
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoOnGray
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen
import timber.log.Timber
import javax.inject.Inject

class UserProfileScreenImpl @Inject constructor() : UserProfileScreen {

    @Composable
    override fun UserProfileScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        UserProfileScreenContent(showMessage = showMessage)
    }

    @Composable
    private fun UserProfileScreenContent(
        showMessage: (Int) -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding()
    ) {
        val context = LocalContext.current

        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(
            textRes = R.string.profile_header,
            isLarge = false,
        )

        Spacer(modifier = Modifier.height(Dimensions.mainVerticalPadding * 2))

        NeuronsTextBlock()

        Spacer(modifier = Modifier.height(Dimensions.mainVerticalPadding * 2))

        SubtitleText(textRes = R.string.authorization_data_section_title)

        Spacer(modifier = Modifier.height(Dimensions.mainVerticalPadding))

        Column(modifier = Modifier.padding(start = Dimensions.mainHorizontalPadding)) {
            LabelText(
                textRes = R.string.title_login,
                isLarge = true
            )

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            BodyText(text = "test@gmail.com", isLarge = true)

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            LabelText(
                textRes = R.string.title_password,
                isLarge = true
            )

            Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

            BodyText(text = "******", isLarge = true)
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledTextButton(
            textRes = R.string.share_button_text,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                disabledContainerColor = psychoGray,
                disabledContentColor = psychoOnGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.mainHorizontalPadding),
            onClick = {
                createShareDataChooser(context)
            }
        )

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        FilledTextButton(
            textRes = R.string.logout_button_text,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                disabledContainerColor = psychoGray,
                disabledContentColor = psychoOnGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.mainHorizontalPadding),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(Dimensions.mainVerticalPadding))
    }

    private fun createShareDataChooser(context: Context) {
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?.let { docs ->
                val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                    type = "text/plain"
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                    val fileUris = docs.listFiles()
                        ?.map {
                            FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileProvider",
                                it,
                            )
                        }
                        ?.let { ArrayList(it) }

                    Timber.tag(TAG).i("Detected file URIs for sharing: $fileUris")

                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share stress data"))
            }
    }

    private companion object {
        val TAG: String = UserProfileScreenImpl::class.java.simpleName
    }
}
