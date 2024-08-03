package ru.miem.psychoEvaluation.feature.trainingPreparing.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface TrainingPreparingScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun TrainingPreparingScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
        navigateToTraining: () -> Unit,
    )
}