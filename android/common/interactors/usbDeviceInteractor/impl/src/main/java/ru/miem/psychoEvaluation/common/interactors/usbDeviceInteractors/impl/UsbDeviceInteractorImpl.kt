package ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.impl

import android.hardware.usb.UsbManager
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.core.usbDevice.api.di.UsbDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.utils.coroutines.withIO
import javax.inject.Inject

class UsbDeviceInteractorImpl @Inject constructor() : UsbDeviceInteractor {

    private val usbDeviceRepository by diApi(UsbDeviceRepositoryDiApi::usbDeviceRepository)
    private val airplaneGameDataAnalysis by diApi(DataAnalysisDiApi::dataAnalysis)

    override suspend fun getAllRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (List<Int>) -> Unit
    ) {
        if (usbDeviceRepository.isNotConnected) {
            usbDeviceRepository.connectToUsbDevice(usbManager)
        }
        val usbDeviceRawData = mutableListOf<Int>()

        withIO {
            usbDeviceRepository.usbDeviceDataFlow.collect { sensorData ->
                usbDeviceRawData.add(sensorData)
                onNewValueEmitted(usbDeviceRawData)
            }
        }
    }

    override suspend fun getRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (Int) -> Unit
    ) {
        if (usbDeviceRepository.isNotConnected) {
            usbDeviceRepository.connectToUsbDevice(usbManager)
        }

        withIO {
            usbDeviceRepository.usbDeviceDataFlow.collect(onNewValueEmitted)
        }
    }

    override suspend fun getNormalizedDeviceData(
        usbManager: UsbManager,
        normalizationFactor: Double,
        onNewValueEmitted: suspend (UsbDeviceData) -> Unit
    ) {
        if (usbDeviceRepository.isNotConnected) {
            usbDeviceRepository.connectToUsbDevice(usbManager)
        }

        withIO {
            usbDeviceRepository.usbDeviceDataFlow.collect { rawData ->
                onNewValueEmitted(
                    UsbDeviceData(
                        rawData,
                        rawData / normalizationFactor
                    )
                )
            }
        }
    }


    override fun disconnect() = usbDeviceRepository.disconnect()
}