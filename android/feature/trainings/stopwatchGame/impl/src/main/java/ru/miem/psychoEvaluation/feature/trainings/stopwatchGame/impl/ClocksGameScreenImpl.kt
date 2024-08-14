package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.findActivity
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingScreenArgs
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.StopwatchGameScreen
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.buttons.ActionButton
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.buttons.BackButton
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.clocks.Stopwatch
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.health.HealthBar
import javax.inject.Inject

class StopwatchGameScreenImpl @Inject constructor() : StopwatchGameScreen {

    @Composable
    override fun StopwatchGameScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingScreenArgs: TrainingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val viewModel: StopwatchGameScreenViewModel = viewModel(
            factory = viewModelFactory {
                StopwatchGameScreenViewModel(
                    usbDeviceInteractor,
                    bleDeviceInteractor,
                )
            }
        )

        val stopwatchGameState = viewModel.stopwatchGameState.collectAsState()
        val sensorDeviceType = viewModel.sensorDeviceType.collectAsState()

        when (sensorDeviceType.value) {
            SensorDeviceType.Usb -> {
//                viewModel.connectToUsbDevice(usbManager = usbManager)
            }
            SensorDeviceType.Bluetooth -> {
                val activity = context.findActivity()
                val deviceHardwareAddress = trainingScreenArgs.bleDeviceHardwareAddress

//                require(activity != null && deviceHardwareAddress != null) {
//                    "Activity $activity and deviceHardwareAddress $deviceHardwareAddress cant be null"
//                }

//                viewModel.retrieveDataFromBluetoothDevice(
//                    activity = activity,
//                    bluetoothAdapter = bluetoothManager.adapter,
//                    bleDeviceHardwareAddress = deviceHardwareAddress,
//                )
            }
            SensorDeviceType.Unknown -> {}
        }

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
            viewModel.startGame()
        }

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        DisposableEffect(viewModel) {
            onDispose {
//                viewModel.disconnect()
            }
        }

        StopwatchGameScreenContent(
            stopwatchGameState = stopwatchGameState.value,
            navigateToTrainingList = { navigateToRoute(Routes.trainingsList) },
            onActionButtonClick = viewModel::clickActionButton
        )
    }

    @Composable
    private fun StopwatchGameScreenContent(
        stopwatchGameState: StopwatchGameState,
        navigateToTrainingList: () -> Unit = {},
        onActionButtonClick: () -> Unit = {},
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .screenPaddings(),
    ) {
        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BackButton(
                modifier = Modifier.size(30.dp),
                onClick = navigateToTrainingList,
            )

            HealthBar(
                stopwatchGameState = stopwatchGameState,
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        TitleText(
            text = stopwatchGameState.timeString,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(60.dp))

        Stopwatch(
            stopwatchGameState = stopwatchGameState,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(60.dp))

        ActionButton(
            textRes = R.string.action_button_text,
            onClick = onActionButtonClick
        )

        Spacer(modifier = Modifier.height(36.dp))

        LabelText(
            textRes = R.string.bottom_description_text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }

    private companion object {
        val TAG: String = StopwatchGameScreenImpl::class.java.simpleName
    }
}