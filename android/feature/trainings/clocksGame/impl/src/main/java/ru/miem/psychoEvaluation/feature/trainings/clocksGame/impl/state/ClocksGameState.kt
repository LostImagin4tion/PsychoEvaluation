package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state

import kotlin.time.Duration

sealed interface ClocksGameState

data class ClocksGameLoading(
    val timeBeforeStart: Duration,
    val progress: Double,
) : ClocksGameState

data class ClocksGameInProgress(
    val clocksTime: Duration,
    val gameTime: String,
    val heartsNumber: Int,
    val jumpCount: Int,
    val successfulReactionCount: Int,
    val reactionTimings: List<Long>,
    val stressData: List<Int>,
    val currentIndicatorType: IndicatorType,
) : ClocksGameState {

    enum class IndicatorType {
        Undefined,
        Success,
        Failure
    }
}

data class ClocksGameEnded(
    val gameTime: String,
    val successPercent: Float,
    val averageReactionTimeString: String, // Millis
    val vigilanceDelta: Long,
    val concentrationDelta: Int,
) : ClocksGameState
