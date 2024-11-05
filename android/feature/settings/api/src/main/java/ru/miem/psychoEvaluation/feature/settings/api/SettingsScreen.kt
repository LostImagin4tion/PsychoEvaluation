package ru.miem.psychoEvaluation.feature.settings.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder

interface SettingsScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun SettingsScreen(
        navigateToRoute: (route: String) -> Unit,
        navigateToRouteWithOptions: (String, NavOptionsBuilder.() -> Unit) -> Unit,
        showMessage: (String) -> Unit
    )
}
