package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl

import android.hardware.usb.UsbManager
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisDiApi
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di.UsbDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.core.utils.coroutines.withIO
import javax.inject.Inject

class UsbDeviceInteractorImpl @Inject constructor() : UsbDeviceInteractor {

    private val usbDeviceRepository by diApi(UsbDeviceRepositoryDiApi::usbDeviceRepository)
    private val airplaneGameDataAnalysis by diApi(DataAnalysisDiApi::dataAnalysis)

    override suspend fun getRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (Int) -> Unit
    ) {
        if (usbDeviceRepository.isNotConnected) {
            usbDeviceRepository.connectToUsbDevice(usbManager)
        }

        withIO {
            usbDeviceRepository.deviceDataFlow.collect(onNewValueEmitted)
        }
    }

    override suspend fun getNormalizedDeviceData(
        usbManager: UsbManager,
        normalizationFactor: Double,
        onNewValueEmitted: suspend (UsbDeviceData) -> Unit
    ) {
        connectToUsbDevice(usbManager)

        withIO {
            usbDeviceRepository.deviceDataFlow.collect { rawData ->
                onNewValueEmitted(
                    UsbDeviceData(
                        rawData,
                        rawData / normalizationFactor
                    )
                )
            }
        }
    }

    override fun connectToUsbDevice(usbManager: UsbManager) {
        if (usbDeviceRepository.isNotConnected) {
            usbDeviceRepository.connectToUsbDevice(usbManager)
        }
    }

    override fun disconnect() = usbDeviceRepository.disconnect()
}
