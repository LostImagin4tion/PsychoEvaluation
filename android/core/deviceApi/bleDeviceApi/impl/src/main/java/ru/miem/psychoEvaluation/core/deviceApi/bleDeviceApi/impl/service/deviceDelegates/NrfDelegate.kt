package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.utils.BleDevices
import timber.log.Timber
import java.io.IOException

class NrfDelegate(
    private val onCharacteristicsCreated: (
        read: BluetoothGattCharacteristic,
        write: BluetoothGattCharacteristic,
    ) -> Unit,
    private val onSerialConnectError: (Exception) -> Unit,
) : DeviceDelegate {

    override fun connectCharacteristics(gattService: BluetoothGattService): Boolean {
        Timber.tag(TAG).d("Connect characteristics of nrf device")

        val rw2 = gattService.getCharacteristic(BleDevices.NordicNRF.BLUETOOTH_LE_NRF_CHAR_RW2)
        val rw3 = gattService.getCharacteristic(BleDevices.NordicNRF.BLUETOOTH_LE_NRF_CHAR_RW3)

        if (rw2 != null && rw3 != null) {
            val rw2prop = rw2.properties
            val rw3prop = rw3.properties

            val rw2write = (rw2prop and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0
            val rw3write = (rw3prop and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0

            Timber.tag(TAG).d("Characteristic properties $rw2prop/$rw3prop")

            if (rw2write && rw3write) {
                onSerialConnectError(IOException("Multiple write characteristics ($rw2prop/$rw3prop)"))
            } else if (rw2write) {
                onCharacteristicsCreated(rw3, rw2)
            } else if (rw3write) {
                onCharacteristicsCreated(rw2, rw3)
            } else {
                onSerialConnectError(IOException("No write characteristic ($rw2prop / $rw3prop)"))
            }
        }
        return true
    }

    private companion object {
        val TAG: String = NrfDelegate::class.java.simpleName
    }
}
