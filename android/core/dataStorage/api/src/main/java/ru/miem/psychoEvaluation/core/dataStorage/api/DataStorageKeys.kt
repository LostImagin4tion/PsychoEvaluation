package ru.miem.psychoEvaluation.core.dataStorage.api

import ru.miem.psychoEvaluation.core.dataStorage.api.internal.DataStorageKey

object DataStorageKeys {

    val refreshToken = string("refresh_token")
    val sensorDeviceConnectionType = string("sensor_device_connectionType")
    val apiAccessToken = string("api_access_token")
}

@Suppress("UnusedPrivateMember")
private fun boolean(key: String, default: Boolean = false) =
    DataStorageKey.DataStorageKeyBoolean(key, default)

private fun string(key: String, default: String = "") =
    DataStorageKey.DataStorageKeyString(key, default)

@Suppress("UnusedPrivateMember")
private fun double(key: String, default: Double = 0.0) =
    DataStorageKey.DataStorageKeyDouble(key, default)

@Suppress("UnusedPrivateMember")
private fun int(key: String, default: Int = 0) =
    DataStorageKey.DataStorageKeyInt(key, default)
