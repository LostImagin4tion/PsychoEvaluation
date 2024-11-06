package ru.miem.psychoEvaluation.common.designSystem.system

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager

val Context.requestPermissionIntentAction: String
    get() = "$packageName.USB_PERMISSION"

fun UsbManager.requestUsbDeviceAccess(context: Context) {
    val usbPermissionIntent = context.createUsbDevicePermissionIntent(
        context.requestPermissionIntentAction
    )
    requestPermission(
        deviceList.values.last(),
        usbPermissionIntent
    )
}

@Suppress("MagicNumber")
private fun Context.createUsbDevicePermissionIntent(intentAction: String): PendingIntent = PendingIntent
    .getBroadcast(
        this,
        42,
        Intent(intentAction),
        PendingIntent.FLAG_IMMUTABLE
    )
