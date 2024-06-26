package ru.miem.psychoEvaluation.feature.settings.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface SettingsScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun SettingsScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    )
}
