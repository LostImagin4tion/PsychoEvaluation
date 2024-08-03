package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.modifiers.screenPaddings
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.findActivity
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceConnectionStatus
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceState
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.ui.BluetoothDeviceItem
import javax.inject.Inject

class BluetoothDeviceManagerScreenImpl @Inject constructor() : BluetoothDeviceManagerScreen {

    @Composable
    override fun BluetoothDeviceManagerScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
        navigateToTrainingPreparing: () -> Unit,
    ) {
        val context = LocalContext.current
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter

        val viewModel: BluetoothDeviceManagerViewModel = viewModel()

        val devices = viewModel.devices.collectAsState(persistentListOf())
        var isAnyDevicesConnected = devices.value
            .any { it.connectionStatus == BluetoothDeviceConnectionStatus.Connected }

        LaunchedEffect(Unit) {
            viewModel.discoverBluetoothDevices(bluetoothAdapter.bluetoothLeScanner)
        }

        BluetoothDeviceManagerScreenContent(
            devices.value,
            isAnyDevicesConnected,
            onDeviceTapped = { device ->
                context.findActivity()
                    ?.let {
                        viewModel.connectToBluetoothDevice(it, bluetoothAdapter, device) {
                            isAnyDevicesConnected = true
                        }
                    }
            },
            navigateToTraining = navigateToTrainingPreparing,
        )
    }

    @Composable
    private fun BluetoothDeviceManagerScreenContent(
        devices: ImmutableList<BluetoothDeviceState>,
        isAnyDeviceConnected: Boolean,
        onDeviceTapped: (device: BluetoothDeviceState) -> Unit,
        navigateToTraining: () -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.screenPaddings()
    ) {
        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(
            textRes = R.string.device_manager_header,
            isLarge = false
        )

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
            }

            FilledTextButton(
                isEnabled = isAnyDeviceConnected,
                textRes = R.string.continue_button_text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.primaryHorizontalPadding)
                    .padding(bottom = Dimensions.primaryVerticalPadding * 2)
                    .align(Alignment.BottomCenter),
                onClick = navigateToTraining,
            )
        }
    }

    private companion object {
        val TAG: String = BluetoothDeviceManagerScreenImpl::class.java.simpleName
    }
}
