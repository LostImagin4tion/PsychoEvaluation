package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state

import java.util.Date
import kotlin.time.Duration

sealed interface StopwatchGameState

data class StopwatchGameLoadingState(
    val timeBeforeStart: Duration,
    val progress: Double,
) : StopwatchGameState

data class StopwatchGameInProgressState(
    val stopwatchTime: Duration,
    val gameDate: Date,
    val gameDuration: Duration,
    val gameDurationString: String,
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

data class StopwatchGameStatisticsState(
    val gameDate: Date,
    val gameDuration: Duration,
    val gameDurationString: String,
    val successPercent: Float,
    val score: Int,
    val averageReactionTimeString: String, // Millis
    val vigilanceDelta: Long,
    val concentrationDelta: Int,
) : StopwatchGameState
