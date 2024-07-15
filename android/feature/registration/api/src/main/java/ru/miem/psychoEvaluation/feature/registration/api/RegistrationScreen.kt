package ru.miem.psychoEvaluation.feature.registration.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface RegistrationScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun RegistrationScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    )
}
