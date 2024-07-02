package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl

import android.bluetooth.le.ScanCallback

fun Int.toScanErrorMessage(): String = when (this) {
    1 -> ScanCallback::SCAN_FAILED_ALREADY_STARTED.name
    2 -> ScanCallback::SCAN_FAILED_APPLICATION_REGISTRATION_FAILED.name
    3 -> ScanCallback::SCAN_FAILED_INTERNAL_ERROR.name
    4 -> ScanCallback::SCAN_FAILED_FEATURE_UNSUPPORTED.name
    5 -> ScanCallback::SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES.name
    6 -> ScanCallback::SCAN_FAILED_SCANNING_TOO_FREQUENTLY.name
    else -> "Unknown"
}