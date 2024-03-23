package ru.miem.psychoEvaluation.feature.trainings.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface TrainingsScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun TrainingsScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    )
}