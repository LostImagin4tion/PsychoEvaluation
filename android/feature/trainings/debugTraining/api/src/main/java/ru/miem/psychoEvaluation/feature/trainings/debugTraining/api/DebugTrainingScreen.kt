package ru.miem.psychoEvaluation.feature.trainings.debugTraining.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface DebugTrainingScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun DebugTrainingScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    )
}
