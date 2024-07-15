package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model

import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDeviceData
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.UsbDeviceData

data class SensorData(
    val rawData: Int,
    val normalizedData: Double
)

fun UsbDeviceData.toSensorData() = SensorData(rawData, normalizedData)

fun BluetoothDeviceData.toSensorData() = SensorData(rawData, normalizedData)
