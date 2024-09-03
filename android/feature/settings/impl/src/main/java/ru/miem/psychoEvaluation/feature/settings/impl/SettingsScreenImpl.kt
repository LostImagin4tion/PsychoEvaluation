package ru.miem.psychoEvaluation.feature.settings.impl

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
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
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val viewModel: SettingsScreenViewModel = viewModel()

        val sensorDeviceType by viewModel.sensorDeviceType.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        SettingsScreenContent(
            sensorDeviceType = sensorDeviceType,
            changeSensorDeviceType = viewModel::changeSensorDeviceType
        )
    }

    @Composable
    private fun SettingsScreenContent(
        sensorDeviceType: SensorDeviceType,
        changeSensorDeviceType: (SensorDeviceType) -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.screenPaddings()
    ) {
        val context = LocalContext.current

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(
            textRes = R.string.settings_header,
            isLarge = false,
        )

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

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
            }

            item {
                Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

                FilledTextButton(
                    textRes = R.string.export_stress_data,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimensions.primaryHorizontalPadding),
                    onClick = {
                        createShareDataChooser(context)
                    }
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
