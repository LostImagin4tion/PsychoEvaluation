package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl

import com.hoho.android.usbserial.driver.FtdiSerialDriver
import com.hoho.android.usbserial.driver.ProbeTable
import com.hoho.android.usbserial.driver.UsbSerialProber

internal object CustomProber {
    val customProber: UsbSerialProber = UsbSerialProber(
        ProbeTable().apply {
            // e.g. device with custom VID+PID
            addProduct(0x1234, 0x0001, FtdiSerialDriver::class.java)
            // e.g. device with custom VID+PID
            addProduct(0x1234, 0x0002, FtdiSerialDriver::class.java)
        }
    )
}
