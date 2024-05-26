package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.core.di.impl.diApi
import timber.log.Timber

class AirplaneGameScreenViewModel : ViewModel() {

    private val usbDeviceInteractor by diApi(UsbDeviceInteractorDiApi::usbDeviceInteractor)

    private val _stressData = MutableStateFlow(UsbDeviceData(0, 0.0))

    val stressData: StateFlow<UsbDeviceData> = _stressData
    val allStress = mutableListOf<Int>()
    val chartModelProducer = CartesianChartModelProducer.build()

    fun isUsbDeviceAccessGranted(
        usbManager: UsbManager,
    ): Boolean {
        val device = usbManager.deviceList.values.lastOrNull()
        return device != null && usbManager.hasPermission(device)
    }

    fun hasConnectedDevices(usbManager: UsbManager) = usbManager.deviceList.isNotEmpty()

    fun connectToUsbDevice(usbManager: UsbManager, screenHeight: Double) {
        viewModelScope.launch {
            usbDeviceInteractor.getNormalizedDeviceData(usbManager, screenHeight) {
                _stressData.emit(it)
                allStress.add(it.rawData)
                chartModelProducer.runTransaction {
                    lineSeries { series(allStress) }
                }
            }
        }
    }

    fun disconnect() = usbDeviceInteractor.disconnect()

    private companion object {
        val TAG: String = AirplaneGameScreenViewModel::class.java.simpleName
    }
}