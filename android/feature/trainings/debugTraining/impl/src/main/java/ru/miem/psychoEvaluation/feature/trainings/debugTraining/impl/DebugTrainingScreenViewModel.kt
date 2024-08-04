package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl

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

class DebugTrainingScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    val chartModelProducer = CartesianChartModelProducer.build()

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
            .launchIn(viewModelScope)
    }

    fun connectToUsbDevice(usbManager: UsbManager) {
        val usbDeviceRawData = mutableListOf<Int>()

        viewModelScope.launch {
            usbDeviceInteractor.getRawDeviceData(usbManager) {
                usbDeviceRawData.add(it)
                chartModelProducer.runTransaction {
                    lineSeries { series(usbDeviceRawData) }
                }
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

        val bleDeviceData = mutableListOf<Int>()

        viewModelScope.launch {
            bleDeviceInteractor.getRawDeviceData {
                bleDeviceData.add(it)
                chartModelProducer.runTransaction {
                    lineSeries { series(bleDeviceData) }
                }
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

    private companion object {
        val TAG: String = DebugTrainingScreenViewModel::class.java.simpleName
    }
}
