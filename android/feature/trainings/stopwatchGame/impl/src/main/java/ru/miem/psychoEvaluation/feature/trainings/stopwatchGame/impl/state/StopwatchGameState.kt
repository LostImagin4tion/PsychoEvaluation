package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state

data class StopwatchGameState(
    val stopwatchTime: Float,
    val timeString: String,
    val heartsNumber: Int,
    val currentIndicatorType: IndicatorType,
)

enum class IndicatorType {
    Undefined,
    Success,
    Failure
}
