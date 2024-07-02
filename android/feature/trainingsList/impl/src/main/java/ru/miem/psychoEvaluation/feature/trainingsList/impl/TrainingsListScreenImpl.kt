package ru.miem.psychoEvaluation.feature.trainingsList.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import android.hardware.usb.UsbManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.dialogs.SystemDialog
import ru.miem.psychoEvaluation.common.designSystem.system.EnableBluetoothIfNeeded
import ru.miem.psychoEvaluation.common.designSystem.system.RequestBluetoothPermissionsIfNeeded
import ru.miem.psychoEvaluation.common.designSystem.system.SystemBroadcastReceiver
import ru.miem.psychoEvaluation.common.designSystem.system.requestPermissionIntentAction
import ru.miem.psychoEvaluation.common.designSystem.system.requestUsbDeviceAccess
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen
import javax.inject.Inject

class TrainingsListScreenImpl @Inject constructor() : TrainingsListScreen {

    @Composable
    override fun TrainingsListScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        val viewModel = TrainingsListScreenViewModel()

        var tappedTraining: String? by remember { mutableStateOf(null) }
        var isDeviceConnected by remember { mutableStateOf(false) }

        tappedTraining?.let {
            ConnectDeviceOnTrainingOpen(
                showMessage,
                viewModel,
                navigateToBluetoothDeviceManager = {
                    val route = Routes.bluetoothDeviceManagerRouteTemplate
                        .format(tappedTraining)
                    navController.navigate(route)
                },
                onDeviceAccessGranted = {
                    isDeviceConnected  = true
                }
            )
        }

        TrainingsScreenContent(
            isDeviceConnected = isDeviceConnected,
            onTrainingTapped = { route ->
                if (isDeviceConnected) {
                    navController.navigate(route)
                } else {
                    tappedTraining = route
                }
            },
            navigateToDebugTraining = { navController.navigate(Routes.debugTraining) },
            navigateToAirplaneGame = { navController.navigate(Routes.airplaneGame) },
        )
    }

    @Composable
    private fun TrainingsScreenContent(
        isDeviceConnected: Boolean,
        onTrainingTapped: (trainingRoute: String) -> Unit,
        navigateToDebugTraining: () -> Unit,
        navigateToAirplaneGame: () -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.primaryHorizontalPadding)
            .imePadding()
    ) {
        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(textRes = R.string.trainings_header, isLarge = false)

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { // === Debug Training ===
                TrainingCard(
                    titleRes = R.string.debug_training_title,
                    descriptionRes = R.string.debug_training_description,
                    imageRes = R.drawable.debug_training_icon,
                    modifier = Modifier.padding(bottom = Dimensions.primaryVerticalPadding),
                    onClick = {
                        if (isDeviceConnected) {
                            navigateToDebugTraining()
                        } else {
                            onTrainingTapped(Routes.debugTraining)
                        }
                    }
                )
            }

            item { // === Concentration Training ===
                TrainingCard(
                    titleRes = R.string.concentration_training_title,
                    descriptionRes = R.string.concentration_training_description,
                    imageRes = R.drawable.concentration_training_icon,
                    onClick = {
                        if (isDeviceConnected) {
                            navigateToAirplaneGame()
                        } else {
                            onTrainingTapped(Routes.airplaneGame)
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun ConnectDeviceOnTrainingOpen(
        showMessage: (String) -> Unit,
        viewModel: TrainingsListScreenViewModel,
        navigateToBluetoothDeviceManager: () -> Unit,
        onDeviceAccessGranted: () -> Unit = {}
    ) {
        val unknownDeviceAlertText = stringResource(ru.miem.psychoEvaluation.common.designSystem.R.string.unknown_device_alert)

        val sensorDeviceType = viewModel.sensorDeviceType.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        when (sensorDeviceType.value) {
            SensorDeviceType.USB -> ConnectUsbDevice(viewModel, onDeviceAccessGranted)
            SensorDeviceType.BLUETOOTH -> ConnectBluetoothDevice(viewModel, navigateToBluetoothDeviceManager)
            SensorDeviceType.UNKNOWN -> showMessage(unknownDeviceAlertText)
            null -> {}
        }
    }

    @Composable
    private fun ConnectUsbDevice(
        viewModel: TrainingsListScreenViewModel,
        onDeviceAccessGranted: () -> Unit,
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

        var isDeviceAccessGranted by remember {
            mutableStateOf(viewModel.isUsbDeviceAccessGranted(usbManager))
        }

        SystemBroadcastReceiver(
            isExported = true,
            systemAction = context.requestPermissionIntentAction,
            onSystemEvent = { _ ->
                if (viewModel.isUsbDeviceAccessGranted(usbManager)) {
                    isDeviceAccessGranted = true
                } else {
                    usbManager.requestUsbDeviceAccess(context)
                }
            }
        )

        LaunchedEffect(isDeviceAccessGranted) {
            if (viewModel.hasConnectedDevices(usbManager)) {
                if (!isDeviceAccessGranted) {
                    usbManager.requestUsbDeviceAccess(context)
                } else {
                    viewModel.connectToUsbDevice(usbManager = usbManager)
                    onDeviceAccessGranted()
                }
            }
        }

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
            }
        }
    }

    @Composable
    private fun ConnectBluetoothDevice(
        viewModel: TrainingsListScreenViewModel,
        navigateToBluetoothDeviceManager: () -> Unit,
    ) {
        val context = LocalContext.current
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter

        var isBluetoothAccessGranted by remember { mutableStateOf(false) }
        var tryRequestingBluetoothPermission by remember { mutableStateOf(false) }

        SystemDialog(
            headerText = stringResource(R.string.dialog_header_text),
            descriptionText = stringResource(R.string.dialog_description_text),
            iconRes = ru.miem.psychoEvaluation.common.designSystem.R.drawable.bluetooth_icon,
            onConfirmTapped = {
                tryRequestingBluetoothPermission = true
            }
        )

        if (tryRequestingBluetoothPermission && !isBluetoothAccessGranted) {
            RequestBluetoothPermissionsIfNeeded { permissionsGranted ->
                tryRequestingBluetoothPermission = false
                isBluetoothAccessGranted = permissionsGranted
            }
        }
        else if (isBluetoothAccessGranted) {
            EnableBluetoothIfNeeded(bluetoothAdapter) {
                navigateToBluetoothDeviceManager()
            }
        }

        // TODO listen for bluetooth state via broadcast receiver
        //  https://developer.android.com/develop/connectivity/bluetooth/setup#kotlin
    }
}
