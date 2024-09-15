package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model

import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import kotlin.time.Duration

data class AirplaneGameScreenState(
    val sensorDeviceType: SensorDeviceType,
    val currentScreen: CurrentScreen,
    val maxGameTime: Duration,
)

enum class CurrentScreen {
    AirplaneGameSettings,
    AirplaneGame,
}
