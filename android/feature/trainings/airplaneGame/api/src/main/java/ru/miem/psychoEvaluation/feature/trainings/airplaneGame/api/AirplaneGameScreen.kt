package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.AirplaneGameScreenArgs

interface AirplaneGameScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun AirplaneGameScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        airplaneGameScreenArgs: AirplaneGameScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    )
}
