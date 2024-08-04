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
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.BluetoothDeviceInteractorDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen
import ru.miem.psychoEvaluation.feature.authorization.api.di.AuthorizationDiApi
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.BluetoothDeviceManagerScreen
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api.di.BluetoothDeviceManagerScreenDiApi
import ru.miem.psychoEvaluation.feature.navigation.api.data.BluetoothDeviceManagerRouteArgs
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.TrainingPreparingRouteArgs
import ru.miem.psychoEvaluation.feature.navigation.api.data.TrainingRouteArgs
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.AirplaneGameScreenArgs
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.BluetoothDeviceManagerScreenArgs
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingPreparingScreenArgs
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import ru.miem.psychoEvaluation.feature.registration.api.di.RegistrationDiApi
import ru.miem.psychoEvaluation.feature.settings.api.SettingsScreen
import ru.miem.psychoEvaluation.feature.settings.api.di.SettingsScreenDiApi
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.di.TrainingPreparingDiApi
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
    val trainingPreparingScreen by diApi(TrainingPreparingDiApi::trainingPreparingScreen)
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
        trainingPreparingScreen = trainingPreparingScreen,
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
    trainingPreparingScreen: TrainingPreparingScreen,
    bluetoothDeviceManagerScreen: BluetoothDeviceManagerScreen,
    trainingsListScreen: TrainingsListScreen,
    debugTrainingScreen: DebugTrainingScreen,
    airplaneGameScreen: AirplaneGameScreen,
) {
    val bleDeviceInteractor by remember {
        diApi(BluetoothDeviceInteractorDiApi::bluetoothDeviceInteractor)
    }

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
            startDestination = remember { Routes.userProfile }
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

            composable(Routes.trainingsList) {
                setupSystemBarColors()
                trainingsListScreen.TrainingsListScreen(
                    bleDeviceInteractor = bleDeviceInteractor,
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage
                )
            }

            composable(
                route = Routes.bluetoothDeviceManagerRouteDeclaration,
                arguments = BluetoothDeviceManagerRouteArgs.args,
            ) { backStackEntry ->
                setupSystemBarColors()
                bluetoothDeviceManagerScreen.BluetoothDeviceManagerScreen(
                    bleDeviceInteractor = bleDeviceInteractor,
                    bluetoothDeviceManagerScreenArgs = BluetoothDeviceManagerScreenArgs(
                        trainingPreparingRoute = backStackEntry.arguments
                            ?.getString(BluetoothDeviceManagerRouteArgs.trainingRoute)
                            ?: throw IllegalArgumentException(
                                "Did not find training preparing route in backstack arguments"
                            ),
                    ),
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage,
                )
            }

            composable(
                route = Routes.trainingPreparingRouteDeclaration,
                arguments = TrainingPreparingRouteArgs.args
            ) { backStackEntry ->
                setupSystemBarColors()
                trainingPreparingScreen.TrainingPreparingScreen(
                    bleDeviceInteractor = bleDeviceInteractor,
                    trainingPreparingScreenArgs = TrainingPreparingScreenArgs(
                        trainingRoute = backStackEntry.arguments
                            ?.getString(TrainingPreparingRouteArgs.trainingRoute)
                            ?: throw IllegalArgumentException(
                                "Did not find training route in backstack arguments"
                            ),
                        bleDeviceHardwareAddress = backStackEntry.arguments
                            ?.getString(TrainingPreparingRouteArgs.bleDeviceHardwareAddress)
                    ),
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage,
                )
            }

            composable(Routes.debugTraining) {
                setupSystemBarColors()
                debugTrainingScreen.DebugTrainingScreen(
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage
                )
            }

            composable(
                route = Routes.airplaneGameRouteDeclaration,
                arguments = TrainingRouteArgs.args,
            ) { backStackEntry ->
                setupSystemBarColors()
                airplaneGameScreen.AirplaneGameScreen(
                    bleDeviceInteractor = bleDeviceInteractor,
                    airplaneGameScreenArgs = AirplaneGameScreenArgs(
                        bleDeviceHardwareAddress = backStackEntry.arguments
                            ?.getString(TrainingRouteArgs.bleDeviceHardwareAddress)
                    ),
                    navigateToRoute = navigateToRoute,
                    showMessage = showMessage,
                )
            }
        }
    }
}
