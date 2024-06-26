package ru.miem.psychoEvaluation.feature.trainingsList.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface TrainingsListScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun TrainingsListScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    )
}
