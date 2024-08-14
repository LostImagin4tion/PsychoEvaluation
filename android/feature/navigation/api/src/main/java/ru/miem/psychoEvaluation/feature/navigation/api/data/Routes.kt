package ru.miem.psychoEvaluation.feature.navigation.api.data

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

@Suppress("ConstPropertyName")
object Routes {
    const val authorization = "authorization"
    const val registration = "registration"
    const val settings = "settings"

    // === Navigation bar destinations ===
    const val userProfile = "userProfile"
    const val statistics = "statistics"
    const val trainingsList = "trainingsList"

    val navigationBarDestinations = listOf(userProfile, statistics, trainingsList)

    // === Trainings ===

    const val debugTraining = "debugTraining"
    const val airplaneGame = "airplaneGame"
    const val stopwatchGame = "stopwatchGame"

    private const val bluetoothDeviceManagerRouteTemplate = "bluetoothDeviceManager?"
    private const val trainingPreparingRouteTemplate = "trainingPreparing?"
    private const val generalTrainingRouteTemplate = "%s?"

    // === Routes with args ===

    val bluetoothDeviceManagerRouteDeclaration = createRoute(
        routeTemplate = bluetoothDeviceManagerRouteTemplate,
        args = BluetoothDeviceManagerRouteArgs.args,
    )

    val trainingPreparingRouteDeclaration = createRoute(
        routeTemplate = trainingPreparingRouteTemplate,
        args = TrainingPreparingRouteArgs.args,
    )

    val debugTrainingRouteDeclaration = createRoute(
        routeTemplate = generalTrainingRouteTemplate.format(debugTraining),
        args = TrainingRouteArgs.args,
    )

    val airplaneGameRouteDeclaration = createRoute(
        routeTemplate = generalTrainingRouteTemplate.format(airplaneGame),
        args = TrainingRouteArgs.args,
    )

    val stopwatchGameRouteDeclaration = createRoute(
        routeTemplate = generalTrainingRouteTemplate.format(stopwatchGame),
        args = TrainingRouteArgs.args,
    )

    val bluetoothDeviceManager = createRoute(
        routeTemplate = bluetoothDeviceManagerRouteTemplate,
        args = BluetoothDeviceManagerRouteArgs.args,
        isUsedForRouteDeclaration = false,
    )

    val trainingPreparing = createRoute(
        routeTemplate = trainingPreparingRouteTemplate,
        args = TrainingPreparingRouteArgs.args,
        isUsedForRouteDeclaration = false,
    )

    val generalTrainingRoute = createRoute(
        routeTemplate = generalTrainingRouteTemplate,
        args = TrainingRouteArgs.args,
        isUsedForRouteDeclaration = false,
    )

    private fun createRoute(
        routeTemplate: String,
        args: List<NamedNavArgument>,
        isUsedForRouteDeclaration: Boolean = true,
    ): String {
        return StringBuilder()
            .apply {
                append(routeTemplate)
                constructPath(args, isUsedForRouteDeclaration)
            }
            .toString()
    }

    private fun StringBuilder.constructPath(
        args: List<NamedNavArgument>,
        declaration: Boolean = true
    ) {
        args.forEachIndexed { index, item ->
            if (declaration) {
                append("${item.name}={${item.name}}")
            } else {
                append("${item.name}=%s")
            }

            if (index != args.lastIndex) {
                append("&")
            }
        }
    }
}

@Suppress("ConstPropertyName")
object BluetoothDeviceManagerRouteArgs {
    const val trainingRoute = "trainingRoute"

    val args = listOf(
        navArgument(trainingRoute) {
            nullable = true
        },
    )
}

@Suppress("ConstPropertyName")
object TrainingPreparingRouteArgs {
    const val trainingRoute = "trainingRoute"
    const val bleDeviceHardwareAddress = "bleDeviceHardwareAddress"

    val args = listOf(
        navArgument(trainingRoute) {
            nullable = true
        },
        navArgument(bleDeviceHardwareAddress) {
            nullable = true
        },
    )
}

@Suppress("ConstPropertyName")
object TrainingRouteArgs {
    const val bleDeviceHardwareAddress = "bleDeviceHardwareAddress"

    val args = listOf(
        navArgument(bleDeviceHardwareAddress) {
            nullable = true
        },
    )
}
