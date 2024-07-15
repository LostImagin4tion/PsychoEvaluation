package ru.miem.psychoEvaluation.feature.trainingsList.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface TrainingsListScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun TrainingsListScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    )
}
