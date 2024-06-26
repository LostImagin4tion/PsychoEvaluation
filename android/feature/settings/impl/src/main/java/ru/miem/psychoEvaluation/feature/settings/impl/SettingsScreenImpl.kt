package ru.miem.psychoEvaluation.feature.settings.impl

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.buttons.SimpleTextButton
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.settings.api.SettingsScreen
import ru.miem.psychoEvaluation.feature.settings.impl.ui.SensorDeviceTypeRadioButton
import timber.log.Timber
import javax.inject.Inject

class SettingsScreenImpl @Inject constructor() : SettingsScreen {

    @Composable
    override fun SettingsScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        val viewModel: SettingsScreenViewModel = viewModel()

        val sensorDeviceType = viewModel.sensorDeviceType.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        SettingsScreenContent(
            sensorDeviceType = sensorDeviceType.value,
            changeSensorDeviceType = viewModel::changeSensorDeviceType
        )
    }

    @Composable
    private fun SettingsScreenContent(
        sensorDeviceType: SensorDeviceType,
        changeSensorDeviceType: (SensorDeviceType) -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding()
    ) {
        val context = LocalContext.current

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(
            textRes = R.string.settings_header,
            isLarge = false,
        )

        Spacer(modifier = Modifier.height(Dimensions.mainVerticalPadding * 2))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                SensorDeviceTypeRadioButton(
                    currentType = sensorDeviceType,
                    optionsList = SensorDeviceType.entries,
                    onItemTapped = changeSensorDeviceType,
                )

                Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = Dimensions.dividerThickness,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                SimpleTextButton(
                    textRes = R.string.export_stress_data,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = {
                        createShareDataChooser(context)
                    }
                )

                Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = Dimensions.dividerThickness,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
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
        val TAG: String = SettingsScreenImpl::class.java.simpleName
    }
}
