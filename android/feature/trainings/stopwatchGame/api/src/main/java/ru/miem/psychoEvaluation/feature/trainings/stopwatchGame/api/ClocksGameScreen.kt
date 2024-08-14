package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingScreenArgs

interface StopwatchGameScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun StopwatchGameScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingScreenArgs: TrainingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    )
}