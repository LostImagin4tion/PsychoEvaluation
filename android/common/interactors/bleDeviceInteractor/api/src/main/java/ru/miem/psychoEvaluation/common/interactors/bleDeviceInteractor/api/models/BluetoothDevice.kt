package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings

data class BluetoothDevice(
    val name: String? = null,
    val hardwareAddress: String,
    val advertisementStatus: BluetoothAdvertisementStatus?,
) {
    override fun equals(other: Any?): Boolean {
        return other is BluetoothDevice && hardwareAddress == other.hardwareAddress
    }

    override fun hashCode(): Int = hardwareAddress.hashCode()
}

enum class BluetoothAdvertisementStatus {
    AVAILABLE,
    NOT_AVAILABLE,
}

@SuppressLint("MissingPermission")
fun ScanResult?.toBluetoothDevice(callbackStatus: BluetoothAdvertisementStatus?) = this
    ?.takeIf { it.isConnectable }
    ?.device
    ?.let {
        BluetoothDevice(
            it.name,
            it.address,
            callbackStatus
        )
    }

fun Int.callbackTypeToStatus(): BluetoothAdvertisementStatus? = when (this) {
    ScanSettings.CALLBACK_TYPE_FIRST_MATCH,
    ScanSettings.CALLBACK_TYPE_ALL_MATCHES,
    ScanSettings.CALLBACK_TYPE_ALL_MATCHES_AUTO_BATCH,
    -> BluetoothAdvertisementStatus.AVAILABLE

    ScanSettings.CALLBACK_TYPE_MATCH_LOST -> BluetoothAdvertisementStatus.NOT_AVAILABLE
    else -> null
}
