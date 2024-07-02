package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models

data class BluetoothDevice(
    val name: String? = null,
    val hardwareAddress: String,
    val status: BluetoothDeviceStatus,
) {
    override fun equals(other: Any?): Boolean {
        return other is BluetoothDevice && hardwareAddress == other.hardwareAddress
    }

    override fun hashCode(): Int = hardwareAddress.hashCode()
}

enum class BluetoothDeviceStatus {
    AVAILABLE,
    NOT_AVAILABLE,
    CONNECTED
}
