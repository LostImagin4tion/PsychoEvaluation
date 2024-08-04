package ru.miem.psychoEvaluation.feature.registration.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder

interface RegistrationScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun RegistrationScreen(
        navigateToRoute: (route: String, builder: NavOptionsBuilder.() -> Unit) -> Unit,
        showMessage: (String) -> Unit,
    )
}
