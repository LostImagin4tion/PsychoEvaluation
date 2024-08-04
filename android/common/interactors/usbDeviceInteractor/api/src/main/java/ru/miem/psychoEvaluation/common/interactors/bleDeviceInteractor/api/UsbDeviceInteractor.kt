package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api

import android.hardware.usb.UsbManager
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.UsbDeviceData

interface UsbDeviceInteractor {

    suspend fun findDataBorders(
        usbManager: UsbManager,
        onCompleted: () -> Unit = {},
    )

    suspend fun getRawDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (Int) -> Unit,
    )

    suspend fun getDeviceData(
        usbManager: UsbManager,
        onNewValueEmitted: suspend (UsbDeviceData) -> Unit,
    )

    fun connectToUsbDevice(usbManager: UsbManager)
    fun disconnect()
}
