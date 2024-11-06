package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model

import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import kotlin.time.Duration

sealed interface AirplaneGameState {
    val sensorDeviceType: SensorDeviceType
    val maxGameTime: Duration?
}

data class AirplaneGameSettingsState(
    override val sensorDeviceType: SensorDeviceType,
    override val maxGameTime: Duration?
) : AirplaneGameState

data class AirplaneGameInProgressState(
    override val sensorDeviceType: SensorDeviceType,
    override val maxGameTime: Duration
) : AirplaneGameState

data class AirplaneGameStatisticsState(
    override val sensorDeviceType: SensorDeviceType,
    override val maxGameTime: Duration?,
    val successPercent: Float,
    val gameTime: String,
    val timeInCorridor: String,
    val timeUpperCorridor: String,
    val timeLowerCorridor: String,
    val numberOfFlightsOutsideCorridor: Int,
) : AirplaneGameState

enum class CurrentScreen {
    AirplaneGameSettings,
    AirplaneGameInProgress,
    AirplaneGameStatistics,
}
