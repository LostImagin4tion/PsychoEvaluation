package ru.miem.psychoEvaluation.feature.navigation.api.data

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

    val trainingsDestinations = listOf(debugTraining, airplaneGame)
}
