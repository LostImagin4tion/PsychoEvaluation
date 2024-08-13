package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models

data class UsbDeviceData(
    val rawData: Int,
    val normalizedData: Double,
    val upperLimit: Double,
    val lowerLimit: Double,
)
