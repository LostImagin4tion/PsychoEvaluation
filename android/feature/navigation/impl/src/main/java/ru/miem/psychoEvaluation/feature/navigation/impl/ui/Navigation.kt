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
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationApi
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.DebugTrainingScreen
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di.DebugTrainingScreenApi
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen
import ru.miem.psychoEvaluation.feature.trainingsList.api.di.TrainingsScreenApi
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen
import ru.miem.psychoEvaluation.feature.userProfile.api.di.UserProfileApi

@Composable
fun Navigation(
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    navController: NavHostController,
    setupSystemBarColors: @Composable () -> Unit,
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
    val userProfileScreen by api(UserProfileApi::userProfileScreen)
    val trainingsScreen by api(TrainingsScreenApi::trainingsListScreen)
    val debugTrainingScreen by api(DebugTrainingScreenApi::debugTrainingScreen)

    NavigationContent(
        paddingValues = paddingValues,
        navController = navController,
        showMessage = showMessage,
        setupSystemBarColors = setupSystemBarColors,
        authorizationScreen = authorizationScreen,
        registrationScreen = registrationScreen,
        userProfileScreen = userProfileScreen,
        trainingsListScreen = trainingsScreen,
        debugTrainingScreen = debugTrainingScreen,
    )
}

@Composable
fun NavigationContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    showMessage: (Int) -> Unit,
    setupSystemBarColors: @Composable () -> Unit,
    authorizationScreen: AuthorizationScreen,
    registrationScreen: RegistrationScreen,
    userProfileScreen: UserProfileScreen,
    trainingsListScreen: TrainingsListScreen,
    debugTrainingScreen: DebugTrainingScreen,
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
                Routes.userProfile
            }
        ) {
            composable(Routes.authorization) {
                setupSystemBarColors()
                authorizationScreen.AuthorizationScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }

            composable(Routes.registration) {
                setupSystemBarColors()
                registrationScreen.RegistrationScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }

            composable(Routes.userProfile) {
                setupSystemBarColors()
                userProfileScreen.UserProfileScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }

            composable(Routes.trainingsList) {
                setupSystemBarColors()
                trainingsListScreen.TrainingsScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }

            composable(Routes.debugTraining) {
                setupSystemBarColors()
                debugTrainingScreen.DebugTrainingScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }
        }
    }
}
