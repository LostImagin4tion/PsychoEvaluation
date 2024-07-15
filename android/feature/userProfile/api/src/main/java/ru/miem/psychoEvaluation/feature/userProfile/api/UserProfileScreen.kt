package ru.miem.psychoEvaluation.feature.userProfile.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

interface UserProfileScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun UserProfileScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    )
}
