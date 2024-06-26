package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.di.UsbDeviceInteractorDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi

class DebugTrainingScreenViewModel : ViewModel() {

    private val usbDeviceInteractor by diApi(UsbDeviceInteractorDiApi::usbDeviceInteractor)

    val chartModelProducer = CartesianChartModelProducer.build()

    fun isUsbDeviceAccessGranted(
        usbManager: UsbManager,
    ): Boolean {
        val device = usbManager.deviceList.values.lastOrNull()
        return device != null && usbManager.hasPermission(device)
    }

    fun hasConnectedDevices(usbManager: UsbManager) = usbManager.deviceList.isNotEmpty()

    fun connectToUsbDevice(usbManager: UsbManager) {
        viewModelScope.launch {
            usbDeviceInteractor.getAllRawDeviceData(usbManager) {
                chartModelProducer.runTransaction {
                    lineSeries { series((it)) }
                }
            }
        }
    }

    fun disconnect() = usbDeviceInteractor.disconnect()

    private companion object {
        val TAG: String = DebugTrainingScreenViewModel::class.java.simpleName
    }
}
