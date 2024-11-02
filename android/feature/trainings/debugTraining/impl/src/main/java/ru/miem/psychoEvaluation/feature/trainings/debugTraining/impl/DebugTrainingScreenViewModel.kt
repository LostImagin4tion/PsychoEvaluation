package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.designSystem.utils.toString
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.util.Calendar

class DebugTrainingScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)
    private var fileOutputWriter: BufferedWriter? = null

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
        setupFileInputStream(activity)

        bleDeviceInteractor.connectToBluetoothDevice(
            activity = activity,
            bluetoothAdapter = bluetoothAdapter,
            deviceHardwareAddress = bleDeviceHardwareAddress,
        )

        val bleDeviceData = mutableListOf<Int>()

        viewModelScope.launch {
            bleDeviceInteractor.getRawDeviceData {
                bleDeviceData.add(it)
                fileOutputWriter?.write("$it\n")
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

    fun closeStream() {
        fileOutputWriter?.let {
            it.flush()
            it.close()
            Timber.tag(TAG).i("Closed file output writer")
        }
        fileOutputWriter = null
    }

    private fun setupFileInputStream(context: Context) {
        try {
            closeStream()

            val datetime = Calendar.getInstance().time.toString("yyyy-MM-dd_HH:mm:ss")
            val filename = "debug-psycho-$datetime.txt"

            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            fileOutputWriter = file.bufferedWriter()

            Timber.tag(TAG).i("Created new file ${file.absolutePath}")
        } catch (e: IOException) {
            Timber.tag(TAG).e("Got IO error while writing data to file: $e ${e.message}")
        }
    }

    private companion object {
        val TAG: String = DebugTrainingScreenViewModel::class.java.simpleName
    }
}
