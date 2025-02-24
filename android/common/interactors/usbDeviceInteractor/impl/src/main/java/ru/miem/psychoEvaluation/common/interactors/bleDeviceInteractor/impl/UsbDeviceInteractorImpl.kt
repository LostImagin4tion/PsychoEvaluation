package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl

import android.hardware.usb.UsbManager
import kotlinx.coroutines.flow.toList
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.Borders
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisDiApi
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.di.UsbDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.core.utils.coroutines.withIO
import javax.inject.Inject

class UsbDeviceInteractorImpl @Inject constructor() : UsbDeviceInteractor {

    private val usbDeviceRepository by diApi(UsbDeviceRepositoryDiApi::usbDeviceRepository)
    private val airplaneGameDataAnalysis by diApi(DataAnalysisDiApi::dataAnalysis)

    override var gsrBreathing: List<Int> = emptyList()

    private var dataBorders: Borders? = null

    override suspend fun findDataBorders(
        usbManager: UsbManager,
        onCompleted: () -> Unit,
    ) {
        connectToUsbDevice(usbManager)

        withIO {
            val preparationData = airplaneGameDataAnalysis.findPreparationData(
                usbDeviceRepository.deviceDataFlow
            )

            dataBorders = preparationData.toList()
                .let {
                    gsrBreathing = it
                    airplaneGameDataAnalysis.findDataBorders(it)
                }
                .also {
                    onCompleted()
                }
        }
    }

    override suspend fun getRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (Int) -> Unit
    ) {
        connectToUsbDevice(usbManager)

        withIO {
            usbDeviceRepository.deviceDataFlow.collect(onNewValueEmitted)
        }
    }

    override suspend fun getDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (UsbDeviceData) -> Unit
    ) {
        connectToUsbDevice(usbManager)

        withIO {
            usbDeviceRepository.deviceDataFlow.collect { rawData ->
                val borders = dataBorders
                check(borders != null) {
                    "Data borders is null. You must detect it before collecting sensor data"
                }

                onNewValueEmitted(
                    UsbDeviceData(
                        rawData = rawData,
                        normalizedData = airplaneGameDataAnalysis.getNormalizedValue(
                            value = rawData.toDouble(),
                            borders = borders
                        ),
                        upperLimit = borders.upperLimit,
                        lowerLimit = borders.lowerLimit,
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

    override fun increaseGameDifficulty() {
        dataBorders = dataBorders?.let { airplaneGameDataAnalysis.increaseDifficulty(it) }
    }

    override fun decreaseGameDifficulty() {
        dataBorders = dataBorders?.let { airplaneGameDataAnalysis.decreaseDifficulty(it) }
    }

    override fun changeDataBorders(upperLimit: Double?, lowerLimit: Double?) {
        dataBorders = dataBorders?.run {
            copy(
                upperLimit = upperLimit
                    ?.let { 1 + it }
                    ?: this.upperLimit,
                lowerLimit = lowerLimit
                    ?.let { 1 - it }
                    ?: this.lowerLimit
            )
        }
    }
}
