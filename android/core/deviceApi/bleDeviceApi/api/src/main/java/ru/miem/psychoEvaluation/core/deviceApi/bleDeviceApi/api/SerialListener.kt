package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api

interface SerialListener {
    fun onSerialConnect()
    fun onSerialConnectError(e: Exception?)
    fun onSerialRead(data: ByteArray)
    fun onSerialIoError(e: Exception?)
}
