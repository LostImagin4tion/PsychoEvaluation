package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api

import android.hardware.usb.UsbManager
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.UsbDeviceData

interface UsbDeviceInteractor {

    val gsrBreathing: List<Int>

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

    fun increaseGameDifficulty()
    fun decreaseGameDifficulty()
    fun changeDataBorders(upperLimit: Double?, lowerLimit: Double?)
}
