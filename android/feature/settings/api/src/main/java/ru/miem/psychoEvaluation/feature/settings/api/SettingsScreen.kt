package ru.miem.psychoEvaluation.feature.settings.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface SettingsScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun SettingsScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    )
}
