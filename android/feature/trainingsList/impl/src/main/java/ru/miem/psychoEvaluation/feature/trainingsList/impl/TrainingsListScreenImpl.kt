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
import timber.log.Timber
import javax.inject.Inject

class TrainingsListScreenImpl @Inject constructor() : TrainingsListScreen {

    @Composable
    override fun TrainingsListScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        val viewModel = TrainingsListScreenViewModel()

        var tappedTraining: String? by remember { mutableStateOf(null) }
        var isUsbDeviceConnected by remember { mutableStateOf(false) }

        Timber.tag(TAG).w("HELLO TRAINING LIST SCREEN")

        tappedTraining?.let { training ->
            ConnectDeviceOnTrainingTapped(
                showMessage,
                viewModel,
                onUsbDeviceAccessGranted = {
                    Timber.tag(TAG).w("HELLO TRAINING LIST SCREEN")
                    isUsbDeviceConnected  = true
                },
                navigateToBluetoothDeviceManager = {
                    Timber.tag(TAG).w("HELLO NAVIGATION TO MANAGER")
                    val route = Routes.bluetoothDeviceManagerRouteTemplate
                        .format(training)
                    navController.navigate(route)
                },
            )
        }

        TrainingsScreenContent(
            isUsbDeviceConnected = isUsbDeviceConnected,
            rememberRouteForBluetoothDeviceManager = { route ->
                tappedTraining = route
            },
            navigateToDebugTraining = { navController.navigate(Routes.debugTraining) },
            navigateToAirplaneGame = { navController.navigate(Routes.airplaneGame) },
        )
    }

    @Composable
    private fun TrainingsScreenContent(
        isUsbDeviceConnected: Boolean,
        rememberRouteForBluetoothDeviceManager: (trainingRoute: String) -> Unit,
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
        Timber.tag("HELLO TRAINING SCREEN CONTENT")

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
                        Timber.tag("HELLO TRAINING TAPPED")
                        if (isUsbDeviceConnected) {
                            navigateToDebugTraining()
                        } else {
                            rememberRouteForBluetoothDeviceManager(Routes.debugTraining)
                        }
                    }
                )
            }

            item { // === Airplane Training ===
                TrainingCard(
                    titleRes = R.string.concentration_training_title,
                    descriptionRes = R.string.concentration_training_description,
                    imageRes = R.drawable.concentration_training_icon,
                    onClick = {
                        Timber.tag("HELLO TRAINING TAPPED")
                        if (isUsbDeviceConnected) {
                            navigateToAirplaneGame()
                        } else {
                            rememberRouteForBluetoothDeviceManager(Routes.airplaneGame)
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun ConnectDeviceOnTrainingTapped(
        showMessage: (String) -> Unit,
        viewModel: TrainingsListScreenViewModel,
        onUsbDeviceAccessGranted: () -> Unit = {},
        navigateToBluetoothDeviceManager: () -> Unit = {},
    ) {
        Timber.tag(TAG).w("HELLO CONNECT DEVICE ON TRAINING TAPPED")
        val unknownDeviceAlertText = stringResource(ru.miem.psychoEvaluation.common.designSystem.R.string.unknown_device_alert)

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        val sensorDeviceType = viewModel.sensorDeviceType.collectAsState()

        when (sensorDeviceType.value) {
            SensorDeviceType.USB -> ConnectUsbDevice(viewModel, onUsbDeviceAccessGranted)
            SensorDeviceType.BLUETOOTH -> ConnectBluetoothDevice(navigateToBluetoothDeviceManager)
            SensorDeviceType.UNKNOWN -> showMessage(unknownDeviceAlertText)
            null -> {}
        }
    }

    @Composable
    private fun ConnectUsbDevice(
        viewModel: TrainingsListScreenViewModel,
        onDeviceAccessGranted: () -> Unit,
    ) {
        Timber.tag(TAG).w("HELLO CONNECT USB DEVICE")
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
        navigateToBluetoothDeviceManager: () -> Unit,
    ) {
        val context = LocalContext.current
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter

        var shouldShowSystemDialog by remember { mutableStateOf(true) }
        var isBluetoothAccessGranted by remember { mutableStateOf(false) }
        var tryRequestingBluetoothPermission by remember { mutableStateOf(false) }

        Timber.tag(TAG).w("HELLO CONNECT BLUETOOTH DEVICE")

        if (shouldShowSystemDialog) {
            SystemDialog(
                headerText = stringResource(R.string.dialog_header_text),
                descriptionText = stringResource(R.string.dialog_description_text),
                iconRes = ru.miem.psychoEvaluation.common.designSystem.R.drawable.bluetooth_icon,
                onConfirm = {
                    Timber.tag(TAG).w("HELLO TAPPED SYSTEM DIALOG CONNECT DEVICE")
                    shouldShowSystemDialog = false
                    tryRequestingBluetoothPermission = true
                },
                onDismiss = {
                    shouldShowSystemDialog = false
                }
            )
        }

        if (tryRequestingBluetoothPermission) {
            Timber.tag(TAG).w("HELLO REQUEST PERMISSIONS IF NEEDED")
            RequestBluetoothPermissionsIfNeeded { permissionsGranted ->
                Timber.tag(TAG).w("HELLO PERMISSION GRANTED? $permissionsGranted")
                tryRequestingBluetoothPermission = false
                isBluetoothAccessGranted = permissionsGranted
            }
        }
        else if (isBluetoothAccessGranted) {
            Timber.tag(TAG).w("HELLO ENABLE BLUETOOTH")
            EnableBluetoothIfNeeded(bluetoothAdapter) {
                Timber.tag(TAG).w("HELLO ON BLUETOOTH ENABLED")
                navigateToBluetoothDeviceManager()
            }
        }

        // TODO listen for bluetooth state via broadcast receiver
        //  https://developer.android.com/develop/connectivity/bluetooth/setup#kotlin
    }

    private companion object {
        val TAG: String = TrainingsListScreenImpl::class.java.simpleName
    }
}
