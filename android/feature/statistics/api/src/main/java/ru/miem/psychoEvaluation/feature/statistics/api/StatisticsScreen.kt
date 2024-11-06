package ru.miem.psychoEvaluation.feature.statistics.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface StatisticsScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun StatisticsScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    )
}
