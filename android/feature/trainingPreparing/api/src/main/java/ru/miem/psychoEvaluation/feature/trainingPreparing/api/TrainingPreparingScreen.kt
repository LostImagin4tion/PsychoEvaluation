package ru.miem.psychoEvaluation.feature.trainingPreparing.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingPreparingScreenArgs

interface TrainingPreparingScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun TrainingPreparingScreen(
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingPreparingScreenArgs: TrainingPreparingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    )
}
