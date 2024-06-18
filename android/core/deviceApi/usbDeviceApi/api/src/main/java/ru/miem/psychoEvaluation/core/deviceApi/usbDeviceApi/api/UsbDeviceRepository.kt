package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api

import android.hardware.usb.UsbManager
import ru.miem.psychoEvaluation.core.deviceApi.api.DeviceApi

interface UsbDeviceRepository : DeviceApi {

    fun connectToUsbDevice(
        usbManager: UsbManager,
        portIndex: Int = 0,
        baudRate: Int = 19200,
        useIOManager: Boolean = true,
    )
}