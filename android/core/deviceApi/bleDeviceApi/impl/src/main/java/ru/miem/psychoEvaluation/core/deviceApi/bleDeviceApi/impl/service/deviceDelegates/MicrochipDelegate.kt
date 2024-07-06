package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.utils.BleDevices
import timber.log.Timber

class MicrochipDelegate(
    private val onCharacteristicsCreated: (read: BluetoothGattCharacteristic, write: BluetoothGattCharacteristic) -> Unit
) : DeviceDelegate() {

    override fun connectCharacteristics(gattService: BluetoothGattService): Boolean {
        Timber.tag(TAG).d("Connect characteristics of microchip device")

        val readCharacteristic = gattService
            .getCharacteristic(BleDevices.BLUETOOTH_LE_MICROCHIP_CHAR_RW)

        val writeCharacteristic = gattService
            .getCharacteristic(BleDevices.BLUETOOTH_LE_MICROCHIP_CHAR_W)
            ?: gattService.getCharacteristic(BleDevices.BLUETOOTH_LE_MICROCHIP_CHAR_RW)

        onCharacteristicsCreated(readCharacteristic, writeCharacteristic)
        return true
    }

    private companion object {
        val TAG: String = MicrochipDelegate::class.java.name
    }
}