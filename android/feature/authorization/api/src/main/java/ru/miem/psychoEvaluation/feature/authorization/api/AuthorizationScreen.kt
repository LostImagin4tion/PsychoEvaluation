package ru.miem.psychoEvaluation.feature.authorization.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface AuthorizationScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun AuthorizationScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit,
    )
}
