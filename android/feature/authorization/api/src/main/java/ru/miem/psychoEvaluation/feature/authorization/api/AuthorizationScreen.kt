package ru.miem.psychoEvaluation.feature.authorization.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder

interface AuthorizationScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun AuthorizationScreen(
        navigateToRoute: (route: String, builder: NavOptionsBuilder.() -> Unit) -> Unit,
        showMessage: (String) -> Unit,
    )
}
