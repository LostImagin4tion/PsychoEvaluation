package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.miem.psychoEvaluation.common.designSystem.buttons.BackButton
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.findActivity
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceConnectionStatus
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceState
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.ui.BluetoothDeviceItem
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.BluetoothDeviceManagerScreenArgs
import javax.inject.Inject

class BluetoothDeviceManagerScreenImpl @Inject constructor() : BluetoothDeviceManagerScreen {

    @Composable
    override fun BluetoothDeviceManagerScreen(
        bleDeviceInteractor: BluetoothDeviceInteractor,
        bluetoothDeviceManagerScreenArgs: BluetoothDeviceManagerScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    ) {
        val context = LocalContext.current
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter

        val viewModel: BluetoothDeviceManagerViewModel = viewModel(
            factory = viewModelFactory { BluetoothDeviceManagerViewModel(bleDeviceInteractor) }
        )

        val devices by viewModel.devices.collectAsStateWithLifecycle(persistentListOf())

        val connectedDevice = devices.firstOrNull {
            it.connectionStatus == BluetoothDeviceConnectionStatus.Connected
        }

        LaunchedEffect(Unit) {
            viewModel.discoverBluetoothDevices(bluetoothAdapter.bluetoothLeScanner)
        }

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        BluetoothDeviceManagerScreenContent(
            devices = devices,
            isAnyDeviceConnected = connectedDevice != null,
            onDeviceTapped = { device ->
                if (device == connectedDevice) {
                    viewModel.disconnectBluetoothDevice(device)
                } else {
                    context.findActivity()?.let {
                        viewModel.connectToBluetoothDevice(it, bluetoothAdapter, device)
                    }
                }
            },
            onBackButtonClick = {
                navigateToRoute(Routes.trainingsList)
            },
            navigateToNextScreen = {
                val route = when (val routeArg = bluetoothDeviceManagerScreenArgs.nextScreenRoute) {
                    Routes.debugTraining,
                    Routes.stopwatchGame,
                    Routes.clocksGame,
                    -> Routes.generalTrainingRoute.format(
                        routeArg,
                        connectedDevice?.hardwareAddress,
                    )
                    else -> Routes.trainingPreparing.format(
                        routeArg,
                        connectedDevice?.hardwareAddress
                    )
                }
                navigateToRoute(route)
            },
        )
    }

    @Composable
    private fun BluetoothDeviceManagerScreenContent(
        devices: ImmutableList<BluetoothDeviceState>,
        isAnyDeviceConnected: Boolean,
        onBackButtonClick: () -> Unit,
        onDeviceTapped: (device: BluetoothDeviceState) -> Unit,
        navigateToNextScreen: () -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.screenPaddings()
    ) {
        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            BackButton(onClick = onBackButtonClick)

            Spacer(modifier = Modifier.width(Dimensions.commonPadding))

            TitleText(
                textRes = R.string.device_manager_header,
                isLarge = false
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.primaryVerticalPadding * 2))

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(devices) { item ->
                    BluetoothDeviceItem(
                        deviceName = item.name,
                        hardwareAddress = item.hardwareAddress,
                        connectionStatus = item.connectionStatus,
                        onClick = {
                            onDeviceTapped(item)
                        },
                    )

                    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))
                }

                item {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }

            FilledTextButton(
                isEnabled = isAnyDeviceConnected,
                textRes = R.string.continue_button_text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.primaryHorizontalPadding)
                    .padding(bottom = Dimensions.primaryVerticalPadding * 2)
                    .align(Alignment.BottomCenter),
                onClick = navigateToNextScreen,
            )
        }
    }

    private companion object {
        val TAG: String = BluetoothDeviceManagerScreenImpl::class.java.simpleName
    }
}
