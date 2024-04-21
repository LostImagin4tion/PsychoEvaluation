package ru.miem.psychoEvaluation.feature.navigation.api.data

object Routes {
    const val authorization = "authorization"
    const val registration = "registration"

    // === Navigation bar destinations ===
    const val userProfile = "userProfile"
    const val statistics = "statistics"
    const val trainingsList = "trainingsList"

    val navigationBarDestinations = listOf(userProfile, statistics, trainingsList)

    // === Trainings ===
    const val debugTraining = "debugTraining"

    val trainingsDestinations = listOf(debugTraining)
}
