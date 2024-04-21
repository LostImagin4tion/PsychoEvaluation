package ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.miem.psychoEvaluation.core.di.impl.api
import ru.miem.psychoEvaluation.core.usbDevice.api.di.UsbDeviceRepositoryApi
import ru.miem.psychoEvaluation.core.usbDevice.api.models.UsbDeviceData

class DebugTrainingScreenViewModel : ViewModel() {

    private val usbDeviceRepository by api(UsbDeviceRepositoryApi::usbDeviceRepository)

    private val stressData = mutableListOf<UsbDeviceData>()

    val chartModelProducer = CartesianChartModelProducer.build()

    fun isUsbDeviceAccessGranted(
        usbManager: UsbManager,
    ): Boolean {
        val device = usbManager.deviceList.values.lastOrNull()
        return usbManager.hasPermission(device)
    }

    fun connectToUsbDevice(usbManager: UsbManager) {
        usbDeviceRepository.connectToUsbDevice(usbManager = usbManager)

        viewModelScope.launch {
            usbDeviceRepository.usbDeviceDataFlow.collect { usbDeviceData ->
                withContext(Dispatchers.Default) {
                    stressData.add(usbDeviceData)
                    chartModelProducer.runTransaction {
                        lineSeries {
                            series(stressData.map { it.data })
                        }
                    }
                }
            }
        }
    }

    fun disconnect() {
        usbDeviceRepository.disconnect()
    }

    private companion object {
        val TAG: String = DebugTrainingScreenViewModel::class.java.simpleName
    }
}
