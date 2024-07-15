package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.BluetoothDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.toSensorData
import timber.log.Timber

class AirplaneGameScreenViewModel : ViewModel() {

    private val usbDeviceInteractor by diApi(UsbDeviceInteractorDiApi::usbDeviceInteractor)
    private val bleDeviceInteractor by diApi(BluetoothDeviceInteractorDiApi::bluetoothDeviceInteractor)
    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _stressData = MutableStateFlow(SensorData(0, 0.0))
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.UNKNOWN)

    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType
    val stressData: StateFlow<SensorData> = _stressData

    val allStress = mutableListOf<Int>()
    val chartModelProducer = CartesianChartModelProducer.build()

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
            .launchIn(viewModelScope)
    }

    fun connectToUsbDevice(usbManager: UsbManager, screenHeight: Double) {
        viewModelScope.launch {
            usbDeviceInteractor.getNormalizedDeviceData(usbManager, screenHeight) {
                emitNewData(it.toSensorData())
            }
        }
    }

    fun retrieveDataFromBluetoothDevice(screenHeight: Double) {
        viewModelScope.launch {
            bleDeviceInteractor.getNormalizedDeviceData(screenHeight) {
                Timber.tag(TAG).d("HELLO new device data $it")
                emitNewData(it.toSensorData())
            }
        }
    }

    fun disconnect() {
        when (_sensorDeviceType.value) {
            SensorDeviceType.USB -> usbDeviceInteractor.disconnect()
            SensorDeviceType.BLUETOOTH -> bleDeviceInteractor.disconnect()
            SensorDeviceType.UNKNOWN -> {}
        }
    }

    private suspend fun emitNewData(data: SensorData) {
        _stressData.emit(data)
        allStress.add(data.rawData)
        chartModelProducer.runTransaction {
            lineSeries { series(allStress) }
        }
    }

    private companion object {
        val TAG: String = AirplaneGameScreenViewModel::class.java.simpleName
    }
}
