package ru.miem.psychoEvaluation.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi

class SettingsScreenViewModel : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.UNKNOWN)

    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        viewModelScope.launch {
            settingsInteractor.getCurrentSensorDeviceType().collect {
                _sensorDeviceType.emit(it)
            }
        }
    }

    fun changeSensorDeviceType(sensorDeviceType: SensorDeviceType) {
        viewModelScope.launch {
            settingsInteractor.changeSensorDeviceType(sensorDeviceType)
        }
    }
}
