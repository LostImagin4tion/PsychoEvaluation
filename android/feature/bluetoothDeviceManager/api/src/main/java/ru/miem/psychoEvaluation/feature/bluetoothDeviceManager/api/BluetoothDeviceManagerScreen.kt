package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface BluetoothDeviceManagerScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun DeviceManagerScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
        navigateToTraining: () -> Unit,
    )
}
