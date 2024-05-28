package ru.miem.psychoEvaluation.core.usbDevice.api

import android.hardware.usb.UsbManager
import kotlinx.coroutines.flow.Flow

interface UsbDeviceRepository {
    val usbDeviceDataFlow: Flow<Int>
    val isConnected: Boolean
    val isNotConnected: Boolean
        get() = !isConnected

    fun connectToUsbDevice(
        usbManager: UsbManager,
        portIndex: Int = 0,
        baudRate: Int = 19200,
        useIOManager: Boolean = true,
    )

    fun disconnect()
}
