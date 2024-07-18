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
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationDiApi
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.di.BluetoothDeviceManagerScreenDiApi
import ru.miem.psychoEvaluation.feature.navigation.api.data.BluetoothDeviceManagerRouteArgs
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationDiApi
import ru.miem.psychoEvaluation.feature.settings.api.SettingsScreen
import ru.miem.psychoEvaluation.feature.settings.api.di.SettingsScreenDiApi
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
    val showMessage: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    val authorizationScreen by diApi(AuthorizationDiApi::authorizationScreen)
    val registrationScreen by diApi(RegistrationDiApi::registrationScreen)
    val userProfileScreen by diApi(UserProfileDiApi::userProfileScreen)
    val settingsScreen by diApi(SettingsScreenDiApi::settingsScreen)
    val deviceManagerScreen by diApi(BluetoothDeviceManagerScreenDiApi::bluetoothDeviceManagerScreen)
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
        settingsScreen = settingsScreen,
        bluetoothDeviceManagerScreen = deviceManagerScreen,
        trainingsListScreen = trainingsScreen,
        debugTrainingScreen = debugTrainingScreen,
        airplaneGameScreen = airplaneGameScreen,
    )
}

@Composable
fun NavigationContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    showMessage: (String) -> Unit,
    setupSystemBarColors: @Composable () -> Unit,
    authorizationScreen: AuthorizationScreen,
    registrationScreen: RegistrationScreen,
    userProfileScreen: UserProfileScreen,
    settingsScreen: SettingsScreen,
    bluetoothDeviceManagerScreen: BluetoothDeviceManagerScreen,
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
        val navigateToRoute: (String) -> Unit = { route ->
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
        val navigateToRouteWithOptions: (String, NavOptionsBuilder.() -> Unit) -> Unit =
            { route, builder ->
                navController.navigate(route) {
                    apply { launchSingleTop = true }
                    apply(builder)
                }
            }

        NavHost(
            navController = navController,
            startDestination = remember { Routes.authorization }
        ) {
            composable(Routes.authorization) {
                setupSystemBarColors()
                authorizationScreen.AuthorizationScreen(
                    navigateToRoute = navigateToRouteWithOptions,
                    showMessage = showMessage
                )
            }

            composable(Routes.registration) {
                setupSystemBarColors()
                registrationScreen.RegistrationScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage
                )
            }

            composable(Routes.userProfile) {
                setupSystemBarColors()
                userProfileScreen.UserProfileScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage
                )
            }

            composable(Routes.settings) {
                setupSystemBarColors()
                settingsScreen.SettingsScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage,
                )
            }

            composable(
                route = Routes.bluetoothDeviceManager,
                arguments = BluetoothDeviceManagerRouteArgs.args,
            ) { backStackEntry ->
                setupSystemBarColors()
                bluetoothDeviceManagerScreen.DeviceManagerScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage,
                    navigateToTraining = {
                        backStackEntry.arguments
                            ?.getString(BluetoothDeviceManagerRouteArgs.trainingRoute)
                            ?.let(navController::navigate)
                    }
                )
            }

            composable(Routes.trainingsList) {
                setupSystemBarColors()
                trainingsListScreen.TrainingsListScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage
                )
            }

            composable(Routes.debugTraining) {
                setupSystemBarColors()
                debugTrainingScreen.DebugTrainingScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage
                )
            }

            composable(Routes.airplaneGame) {
                setupSystemBarColors()
                airplaneGameScreen.AirplaneGameScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage,
                )
            }
        }
    }
}
