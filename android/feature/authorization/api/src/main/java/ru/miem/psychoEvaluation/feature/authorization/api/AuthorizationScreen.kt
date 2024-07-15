package ru.miem.psychoEvaluation.feature.authorization.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface AuthorizationScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun AuthorizationScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    )
}
