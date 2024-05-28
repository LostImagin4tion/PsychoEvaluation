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
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationDiApi
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationDiApi
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreenDiApi
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.DebugTrainingScreen
import ru.miem.psychoEvaluation.feature.trainings.debugTraining.api.di.DebugTrainingScreenDiApi
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen
import ru.miem.psychoEvaluation.feature.trainingsList.api.di.TrainingsScreenDiApi
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen
import ru.miem.psychoEvaluation.feature.userProfile.api.di.UserProfileDiApi

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

    val authorizationScreen by diApi(AuthorizationDiApi::authorizationScreen)
    val registrationScreen by diApi(RegistrationDiApi::registrationScreen)
    val userProfileScreen by diApi(UserProfileDiApi::userProfileScreen)
    val trainingsScreen by diApi(TrainingsScreenDiApi::trainingsListScreen)
    val debugTrainingScreen by diApi(DebugTrainingScreenDiApi::debugTrainingScreen)
    val airplaneGameScreen by diApi(AirplaneGameScreenDiApi::airplaneGameScreen)

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
        airplaneGameScreen = airplaneGameScreen,
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
    airplaneGameScreen: AirplaneGameScreen,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = remember { Routes.authorization }
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
                trainingsListScreen.TrainingsListScreen(
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

            composable(Routes.airplaneGame) {
                setupSystemBarColors()
                airplaneGameScreen.AirplaneGameScreen(
                    navController = navController,
                    showMessage = showMessage,
                )
            }
        }
    }
}
