package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.utils.findActivity
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingScreenArgs
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameInProgressState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameSettingsState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.AirplaneGameStatisticsState
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.ui.screens.AirplaneGameInProgressScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.ui.screens.AirplaneGameSettingsScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.ui.screens.AirplaneGameStatisticsScreen
import javax.inject.Inject

class AirplaneGameScreenImpl @Inject constructor() : AirplaneGameScreen {

    @Composable
    override fun AirplaneGameScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingScreenArgs: TrainingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val viewModel: AirplaneGameScreenViewModel = viewModel(
            factory = viewModelFactory {
                AirplaneGameScreenViewModel(
                    usbDeviceInteractor,
                    bleDeviceInteractor,
                )
            }
        )

        val gameScreenState by viewModel.gameScreenState.collectAsStateWithLifecycle()

        when (gameScreenState.sensorDeviceType) {
            SensorDeviceType.Usb -> {
                viewModel.connectToUsbDevice(usbManager = usbManager)
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

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
            }
        }

        gameScreenState.let { state ->
            when (state) {
                is AirplaneGameSettingsState -> AirplaneGameSettingsScreen(
                    onBackClick = { navigateToRoute(Routes.trainingsList) },
                    onContinueButtonClick = { upperBound, lowerBound, gameTime ->
                        viewModel.changeGameParameters(upperBound, lowerBound, gameTime)
                    }
                )

                is AirplaneGameInProgressState -> AirplaneGameInProgressScreen(
                    showMessage = showMessage,
                    dataFlow = viewModel.stressData,
                    modelProducer = viewModel.chartModelProducer,
                    state = state,
                    onGameEnded = {
                            gameTime,
                            gameDate,
                            timeInCorridor,
                            timeUpperCorridor,
                            timeLowerCorridor,
                            numberOfFlightsOutsideCorridor,
                        ->
                        viewModel.finishGame(
                            gameTime,
                            gameDate,
                            timeInCorridor,
                            timeUpperCorridor,
                            timeLowerCorridor,
                            numberOfFlightsOutsideCorridor,
                        )
                    },
                    onStartButtonClick = {
                        viewModel.startGame()
                    },
                    onSettingsButtonClick = {
                        viewModel.changeScreen(CurrentScreen.AirplaneGameSettings)
                    },
                    onStatisticsButtonClick = {
                            gameTime,
                            timeInCorridor,
                            timeUpperCorridor,
                            timeLowerCorridor,
                            numberOfFlightsOutsideCorridor,
                        ->
                        viewModel.goToStatisticsScreen(
                            gameTime,
                            timeInCorridor,
                            timeUpperCorridor,
                            timeLowerCorridor,
                            numberOfFlightsOutsideCorridor,
                        )
                    },
                    onExitButtonClick = {
                        navigateToRoute(Routes.trainingsList)
                    },
                    increaseGameDifficulty = viewModel::increaseGameDifficulty,
                    decreaseGameDifficulty = viewModel::decreaseGameDifficulty
                )

                is AirplaneGameStatisticsState -> AirplaneGameStatisticsScreen(
                    state = state,
                    restartGame = { viewModel.restartGame() },
                    navigateToSettings = {
                        viewModel.changeScreen(CurrentScreen.AirplaneGameSettings)
                    },
                    navigateToTrainingListScreen = { navigateToRoute(Routes.trainingsList) }
                )
            }
        }
    }

    companion object {
        val TAG: String = AirplaneGameScreenImpl::class.java.simpleName
    }
}
