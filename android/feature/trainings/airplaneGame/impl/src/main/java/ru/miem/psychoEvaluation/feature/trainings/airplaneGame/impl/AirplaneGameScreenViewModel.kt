package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
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
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.toSensorData

class AirplaneGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _stressData = MutableStateFlow(defaultSensorData)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType
    val stressData: StateFlow<SensorData> = _stressData

    private val allStress = mutableListOf<Int>()
    val chartModelProducer = CartesianChartModelProducer.build()

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
            .launchIn(viewModelScope)
    }

    fun connectToUsbDevice(usbManager: UsbManager) {
        viewModelScope.launch {
            usbDeviceInteractor.getDeviceData(usbManager) {
                emitNewData(it.toSensorData())
            }
        }
    }

    fun retrieveDataFromBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        bleDeviceHardwareAddress: String,
    ) {
        bleDeviceInteractor.connectToBluetoothDevice(
            activity = activity,
            bluetoothAdapter = bluetoothAdapter,
            deviceHardwareAddress = bleDeviceHardwareAddress,
        )

        viewModelScope.launch {
            bleDeviceInteractor.getDeviceData {
                emitNewData(it.toSensorData())
            }
        }
    }

    fun disconnect() {
        when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.disconnect()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.disconnect()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun increaseGameDifficulty() {
        when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.increaseGameDifficulty()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.increaseGameDifficulty()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun decreaseGameDifficulty() {
        when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.decreaseGameDifficulty()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.decreaseGameDifficulty()
            SensorDeviceType.Unknown -> {}
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

        val defaultSensorData = SensorData(0, 0.0, 0.0, 0.0)
    }
}
