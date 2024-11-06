package ru.miem.psychoEvaluation.core.deviceApi.api

import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    val deviceDataFlow: Flow<Int>
    val isConnected: Boolean
    val isNotConnected: Boolean
        get() = !isConnected

    fun disconnect()
}
