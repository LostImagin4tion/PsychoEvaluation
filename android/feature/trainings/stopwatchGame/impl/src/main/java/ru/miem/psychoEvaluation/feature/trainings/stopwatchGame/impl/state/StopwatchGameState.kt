package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state

import kotlin.time.Duration

sealed interface StopwatchGameState

data class StopwatchGameLoading(
    val timeBeforeStart: Duration,
    val progress: Double,
) : StopwatchGameState

data class StopwatchGameInProgress(
    val stopwatchTime: Duration,
    val gameTime: String,
    val heartsNumber: Int,
    val jumpCount: Int,
    val successfulReactionCount: Int,
    val reactionTimings: List<Long>,
    val stressData: List<Int>,
    val currentIndicatorType: IndicatorType,
) : StopwatchGameState {

    enum class IndicatorType {
        Undefined,
        Success,
        Failure
    }
}

data class StopwatchGameEnded(
    val gameTime: String,
    val successPercent: Float,
    val averageReactionTimeString: String, // Millis
    val vigilanceDelta: Long,
    val concentrationDelta: Int,
) : StopwatchGameState
