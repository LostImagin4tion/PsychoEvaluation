package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface AirplaneGameScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun AirplaneGameScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    )
}
