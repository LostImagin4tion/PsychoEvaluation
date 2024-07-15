package ru.miem.psychoEvaluation.feature.navigation.api.data

import androidx.navigation.navArgument

object Routes {
    const val authorization = "authorization"
    const val registration = "registration"
    const val settings = "settings"

    val bluetoothDeviceManagerRouteTemplate = "bluetoothDeviceManager?" +
        "${BluetoothDeviceManagerRouteArgs.trainingRoute}=%s"
            .format(
                BluetoothDeviceManagerRouteArgs.trainingRoute,
            )

    val bluetoothDeviceManager = bluetoothDeviceManagerRouteTemplate
        .format(
            BluetoothDeviceManagerRouteArgs.trainingRoute,
        )

    // === Navigation bar destinations ===
    const val userProfile = "userProfile"
    const val statistics = "statistics"
    const val trainingsList = "trainingsList"

    val navigationBarDestinations = listOf(userProfile, statistics, trainingsList)

    // === Trainings ===
    const val debugTraining = "debugTraining"
    const val airplaneGame = "airplaneGame"

    val trainingsDestinations = listOf(debugTraining, airplaneGame)
}

object BluetoothDeviceManagerRouteArgs {
    const val trainingRoute = "trainingRoute"

    val args = listOf(
        navArgument(trainingRoute) {
            defaultValue = Routes.airplaneGame
        },
    )
}
