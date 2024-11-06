package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import ru.miem.psychoEvaluation.common.designSystem.buttons.BackButton
import ru.miem.psychoEvaluation.common.designSystem.charts.SingleLineChart
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.findActivity
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingScreenArgs
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.DebugTrainingScreen
import javax.inject.Inject

class DebugTrainingScreenImpl @Inject constructor() : DebugTrainingScreen {

    @Composable
    override fun DebugTrainingScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingScreenArgs: TrainingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        navigateBack: () -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val viewModel: DebugTrainingScreenViewModel = viewModel(
            factory = viewModelFactory {
                DebugTrainingScreenViewModel(
                    usbDeviceInteractor,
                    bleDeviceInteractor,
                )
            }
        )

        val sensorDeviceType by viewModel.sensorDeviceType.collectAsStateWithLifecycle()
        val minY by viewModel.minY.collectAsStateWithLifecycle()

        LaunchedEffect(sensorDeviceType) {
            when (sensorDeviceType) {
                SensorDeviceType.Usb -> {
                    val activity = context.findActivity()

                    require(activity != null) {
                        "Activity $activity cant be null"
                    }
                    viewModel.connectToUsbDevice(activity = activity, usbManager = usbManager)
                }

                SensorDeviceType.Bluetooth -> {
                    val activity = context.findActivity()
                    val deviceHardwareAddress = trainingScreenArgs.bleDeviceHardwareAddress

                    require(activity != null && deviceHardwareAddress != null) {
                        "Activity $activity and deviceHardwareAddress $deviceHardwareAddress cant be null"
                    }

                    viewModel.retrieveDataFromBluetoothDevice(
                        activity = activity,
                        bluetoothAdapter = bluetoothManager.adapter,
                        bleDeviceHardwareAddress = deviceHardwareAddress,
                    )
                }

                SensorDeviceType.Unknown -> {}
            }
        }

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
                viewModel.closeStream()
            }
        }

        DebugTrainingScreenContent(
            modelProducer = viewModel.chartModelProducer,
            minY = minY,
            onBackButtonClick = navigateBack,
        )
    }

    @Composable
    private fun DebugTrainingScreenContent(
        modelProducer: CartesianChartModelProducer,
        minY: Int,
        onBackButtonClick: () -> Unit,
    ) = Column(
        modifier = Modifier.screenPaddings()
    ) {
        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        BackButton(onClick = onBackButtonClick)

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        SingleLineChart(
            modelProducer = modelProducer,
            minY = minY,
            modifier = Modifier.fillMaxSize()
        )
    }

    private companion object {
        val TAG: String = DebugTrainingScreenImpl::class.java.simpleName
    }
}
