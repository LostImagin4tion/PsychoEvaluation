package ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.SettingsInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorageKeys
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import javax.inject.Inject

class SettingsInteractorImpl @Inject constructor() : SettingsInteractor {

    private val dataStorage by diApi(DataStorageDiApi::dataStorage)

    override suspend fun changeSensorDeviceType(sensorDeviceType: SensorDeviceType) {
        dataStorage.set(DataStorageKeys.sensorDeviceConnectionType, sensorDeviceType.name)
    }

    override fun getCurrentSensorDeviceType(): Flow<SensorDeviceType> {
        return dataStorage[DataStorageKeys.sensorDeviceConnectionType]
            .map {  data ->
                SensorDeviceType.entries
                    .find { it.name == data }
                    ?: SensorDeviceType.UNKNOWN
            }
    }
}