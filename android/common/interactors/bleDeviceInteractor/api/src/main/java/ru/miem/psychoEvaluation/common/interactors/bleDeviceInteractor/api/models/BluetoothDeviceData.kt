package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models

data class BluetoothDeviceData(
    val rawData: Int,
    val normalizedData: Double,
    val upperLimit: Double,
    val lowerLimit: Double,
)
