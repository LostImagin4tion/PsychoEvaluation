package ru.miem.psychoEvaluation.core.usbDevice.api

import android.hardware.usb.UsbManager
import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.core.usbDevice.api.models.UsbDeviceData

interface UsbDeviceRepository {
    val usbDeviceDataFlow: Flow<UsbDeviceData>

    fun connectToUsbDevice(
        usbManager: UsbManager,
        portIndex: Int = 0,
        baudRate: Int = 19200,
        useIOManager: Boolean = true,
    )

    fun disconnect()
}
