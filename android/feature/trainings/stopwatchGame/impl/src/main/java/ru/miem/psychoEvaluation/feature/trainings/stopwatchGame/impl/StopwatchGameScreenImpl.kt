package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingScreenArgs
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api.StopwatchGameScreen
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameEnded
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameInProgress
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameLoading
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.screens.StopwatchGameLoaderScreen
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.screens.StopwatchGameScreenContent
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.ui.screens.StopwatchGameStatisticsScreen
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

        val stopwatchGameState by viewModel.stopwatchGameState.collectAsStateWithLifecycle()
        val sensorDeviceType by viewModel.sensorDeviceType.collectAsStateWithLifecycle()

        when (sensorDeviceType) {
            SensorDeviceType.Usb -> {
                viewModel.connectToUsbDevice(usbManager = usbManager)
            }
            SensorDeviceType.Bluetooth -> {
//                val activity = context.findActivity()
//                val deviceHardwareAddress = trainingScreenArgs.bleDeviceHardwareAddress

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
            viewModel.startTimerBeforeStart()
        }

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
            }
        }

        stopwatchGameState.let { state ->
            when (state) {
                is StopwatchGameLoading -> StopwatchGameLoaderScreen(
                    state = state,
                )
                is StopwatchGameInProgress -> StopwatchGameScreenContent(
                    state = state,
                    navigateToTrainingList = { navigateToRoute(Routes.trainingsList) },
                    onActionButtonClick = viewModel::clickActionButton
                )
                is StopwatchGameEnded -> StopwatchGameStatisticsScreen(
                    state = state,
                    restartGame = { viewModel.restartGame() },
                    navigateToTrainingListScreen = { navigateToRoute(Routes.trainingsList) }
                )
            }
        }
    }

    private companion object {
        val TAG: String = StopwatchGameScreenImpl::class.java.simpleName
    }
}
