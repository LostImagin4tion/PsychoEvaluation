package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface BluetoothDeviceManagerScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun DeviceManagerScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit,
        navigateToTraining: () -> Unit,
    )
}