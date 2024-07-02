package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.buttons.FilledTextButton
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.ui.BluetoothDeviceItem
import javax.inject.Inject

class BluetoothDeviceManagerScreenImpl @Inject constructor() : BluetoothDeviceManagerScreen {

    @Composable
    override fun DeviceManagerScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit,
        navigateToTraining: () -> Unit,
    ) {
        val context = LocalContext.current
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter

        val viewModel: BluetoothDeviceManagerViewModel = viewModel()

        var isAnyDevicesConnected by remember { mutableStateOf(false) }
        val devices = viewModel.devices.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.discoverBluetoothDevices(bluetoothAdapter.bluetoothLeScanner)
        }

        DeviceManagerScreenContent(
            devices.value.values.toList(),
            isAnyDevicesConnected,
            onDeviceTapped = {

            },
            navigateToTraining = navigateToTraining,
        )
    }

    @Composable
    private fun DeviceManagerScreenContent(
        devices: List<BluetoothDevice>,
        isAnyDeviceConnected: Boolean,
        onDeviceTapped: () -> Unit,
        navigateToTraining: () -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.primaryHorizontalPadding)
            .imePadding(),
    ) {
        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(textRes = R.string.device_manager_header, isLarge = false)

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
                        onClick = {
                            onDeviceTapped()
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
}
