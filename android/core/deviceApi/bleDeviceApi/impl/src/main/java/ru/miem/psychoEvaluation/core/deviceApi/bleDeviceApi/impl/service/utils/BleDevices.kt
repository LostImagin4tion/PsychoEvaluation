package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.utils

import java.util.UUID

object BleDevices {
    val BLUETOOTH_LE_CCCD: UUID =
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    object CC254x {
        val BLUETOOTH_LE_CC254X_SERVICE: UUID =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")

        val BLUETOOTH_LE_CC254X_CHAR_RW: UUID =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    }

    object NordicNRF {
        val BLUETOOTH_LE_NRF_SERVICE: UUID =
            UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")

        // read on microbit, write on adafruit
        val BLUETOOTH_LE_NRF_CHAR_RW2: UUID =
            UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")

        val BLUETOOTH_LE_NRF_CHAR_RW3: UUID =
            UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    }

    object Microchip {
        val BLUETOOTH_LE_MICROCHIP_SERVICE: UUID =
            UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455")

        val BLUETOOTH_LE_MICROCHIP_CHAR_RW: UUID =
            UUID.fromString("49535343-1E4D-4BD9-BA61-23C647249616")

        val BLUETOOTH_LE_MICROCHIP_CHAR_W: UUID =
            UUID.fromString("49535343-8841-43F4-A8D4-ECBE34729BB3")
    }

    object Telit {
        // https://play.google.com/store/apps/details?id=com.telit.tiosample
        // https://www.telit.com/wp-content/uploads/2017/09/TIO_Implementation_Guide_r6.pdf
        val BLUETOOTH_LE_TIO_SERVICE: UUID =
            UUID.fromString("0000FEFB-0000-1000-8000-00805F9B34FB")

        val BLUETOOTH_LE_TIO_CHAR_TX =
            UUID.fromString("00000001-0000-1000-8000-008025000000") // WNR

        val BLUETOOTH_LE_TIO_CHAR_RX =
            UUID.fromString("00000002-0000-1000-8000-008025000000") // N

        val BLUETOOTH_LE_TIO_CHAR_TX_CREDITS =
            UUID.fromString("00000003-0000-1000-8000-008025000000") // W

        val BLUETOOTH_LE_TIO_CHAR_RX_CREDITS =
            UUID.fromString("00000004-0000-1000-8000-008025000000") // I
    }
}
