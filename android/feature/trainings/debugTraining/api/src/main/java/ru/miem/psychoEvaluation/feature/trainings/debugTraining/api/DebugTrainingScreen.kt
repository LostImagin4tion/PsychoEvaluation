package ru.miem.psychoEvaluation.feature.trainings.debugTraining.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface DebugTrainingScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun DebugTrainingScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    )
}
