package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.BluetoothDeviceManagerScreenArgs

interface BluetoothDeviceManagerScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun BluetoothDeviceManagerScreen(
        bleDeviceInteractor: BluetoothDeviceInteractor,
        bluetoothDeviceManagerScreenArgs: BluetoothDeviceManagerScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        navigateBack: () -> Unit,
        showMessage: (String) -> Unit,
    )
}
