package ru.miem.psychoEvaluation.feature.navigation.impl.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.core.di.impl.api
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationApi
import ru.miem.psychoEvaluation.feature.navigation.impl.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationApi

@Composable
fun Navigation(
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showMessage: (Int) -> Unit = { message ->
        val strMessage = context.getString(message)
        scope.launch {
            snackbarHostState.showSnackbar(strMessage)
        }
    }

    val authorizationScreen by api(AuthorizationApi::authorizationScreen)
    val registrationScreen by api(RegistrationApi::registrationScreen)

    NavigationContent(
        paddingValues = paddingValues,
        navController = navController,
        showMessage = showMessage,
        authorizationScreen = authorizationScreen,
        registrationScreen = registrationScreen
    )
}

@Composable
fun NavigationContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    showMessage: (Int) -> Unit,
    authorizationScreen: AuthorizationScreen,
    registrationScreen: RegistrationScreen
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = remember {
                Routes.authorization
            }
        ) {
            composable(Routes.authorization) {
                authorizationScreen.AuthorizationScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }

            composable(Routes.registration) {
                registrationScreen.RegistrationScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }
        }
    }
}
