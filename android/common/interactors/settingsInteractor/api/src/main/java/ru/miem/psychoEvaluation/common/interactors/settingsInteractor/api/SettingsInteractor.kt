package ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api

import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType

interface SettingsInteractor {
    suspend fun resetApiTokens()
    suspend fun changeSensorDeviceType(sensorDeviceType: SensorDeviceType)
    fun getCurrentSensorDeviceType(): Flow<SensorDeviceType>
}
