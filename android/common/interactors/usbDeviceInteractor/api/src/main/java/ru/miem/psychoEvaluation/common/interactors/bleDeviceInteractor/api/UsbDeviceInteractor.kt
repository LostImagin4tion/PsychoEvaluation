package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api

import android.hardware.usb.UsbManager
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.UsbDeviceData

interface UsbDeviceInteractor {

    suspend fun getAllRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (List<Int>) -> Unit
    )

    suspend fun getRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (Int) -> Unit
    )

    suspend fun getNormalizedDeviceData(
        usbManager: UsbManager,
        normalizationFactor: Double,
        onNewValueEmitted: suspend (UsbDeviceData) -> Unit
    )

    fun connectToUsbDevice(usbManager: UsbManager)
    fun disconnect()
}
