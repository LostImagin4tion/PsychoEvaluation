package ru.miem.psychoEvaluation.feature.trainingsList.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor

interface TrainingsListScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun TrainingsListScreen(
        bleDeviceInteractor: BluetoothDeviceInteractor,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    )
}
