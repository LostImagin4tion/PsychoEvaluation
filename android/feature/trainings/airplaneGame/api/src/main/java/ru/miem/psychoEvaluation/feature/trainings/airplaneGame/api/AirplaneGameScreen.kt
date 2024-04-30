package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface AirplaneGameScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun AirplaneGameScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit,
    )
}