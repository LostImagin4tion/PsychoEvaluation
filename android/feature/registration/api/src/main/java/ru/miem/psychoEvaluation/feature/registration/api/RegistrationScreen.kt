package ru.miem.psychoEvaluation.feature.registration.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface RegistrationScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun RegistrationScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit,
    )
}
