package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.utils

import android.bluetooth.le.ScanCallback

fun Int.toScanErrorMessage(): String = when (this) {
    ScanCallback.SCAN_FAILED_ALREADY_STARTED ->
        ScanCallback::SCAN_FAILED_ALREADY_STARTED.name

    ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED ->
        ScanCallback::SCAN_FAILED_APPLICATION_REGISTRATION_FAILED.name

    ScanCallback.SCAN_FAILED_INTERNAL_ERROR ->
        ScanCallback::SCAN_FAILED_INTERNAL_ERROR.name

    ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED ->
        ScanCallback::SCAN_FAILED_FEATURE_UNSUPPORTED.name

    ScanCallback.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES ->
        ScanCallback::SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES.name

    ScanCallback.SCAN_FAILED_SCANNING_TOO_FREQUENTLY ->
        ScanCallback::SCAN_FAILED_SCANNING_TOO_FREQUENTLY.name

    else -> "Unknown"
}
