package ru.miem.psychoEvaluation.feature.userProfile.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface UserProfileScreen {

    @Composable
    @SuppressLint("NotConstructor")
    fun UserProfileScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    )
}