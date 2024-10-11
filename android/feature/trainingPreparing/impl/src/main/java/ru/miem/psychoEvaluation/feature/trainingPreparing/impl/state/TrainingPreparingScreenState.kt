package ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TrainingPreparingScreenState(
    val currentScreen: CurrentScreen,
    val roundNumberString: String,
)

enum class CurrentScreen(val durationTime: Duration) {
    Welcome(0.seconds),
    TakeABreath(4.seconds),
    HoldYourBreath(7.seconds),
    Exhale(8.seconds)
}
