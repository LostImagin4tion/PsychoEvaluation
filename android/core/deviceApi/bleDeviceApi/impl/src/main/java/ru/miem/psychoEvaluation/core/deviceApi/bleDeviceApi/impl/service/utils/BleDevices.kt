package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.utils

import java.util.UUID

object BleDevices {
    val BLUETOOTH_LE_CCCD: UUID =
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    val BLUETOOTH_LE_CC254X_SERVICE: UUID =
        UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")

    val BLUETOOTH_LE_CC254X_CHAR_RW: UUID =
        UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")

    val BLUETOOTH_LE_NRF_SERVICE: UUID =
        UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")

    // read on microbit, write on adafruit
    val BLUETOOTH_LE_NRF_CHAR_RW2: UUID =
        UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")

    val BLUETOOTH_LE_NRF_CHAR_RW3: UUID =
        UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")

    val BLUETOOTH_LE_MICROCHIP_SERVICE: UUID =
        UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455")

    val BLUETOOTH_LE_MICROCHIP_CHAR_RW: UUID =
        UUID.fromString("49535343-1E4D-4BD9-BA61-23C647249616")

    val BLUETOOTH_LE_MICROCHIP_CHAR_W: UUID =
        UUID.fromString("49535343-8841-43F4-A8D4-ECBE34729BB3")
}
