package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.utils.BleDevices
import timber.log.Timber

class Cc245XDelegate(
    private val onCharacteristicsCreated: (
        read: BluetoothGattCharacteristic,
        write: BluetoothGattCharacteristic,
    ) -> Unit,
) : DeviceDelegate {

    override fun connectCharacteristics(gattService: BluetoothGattService): Boolean {
        Timber.tag(TAG).d("Connect characteristics of cc254x device")

        val readCharacteristic = gattService.getCharacteristic(BleDevices.CC254x.BLUETOOTH_LE_CC254X_CHAR_RW)
        val writeCharacteristic = gattService.getCharacteristic(BleDevices.CC254x.BLUETOOTH_LE_CC254X_CHAR_RW)
        onCharacteristicsCreated(readCharacteristic, writeCharacteristic)
        return true
    }

    private companion object {
        val TAG: String = Cc245XDelegate::class.java.name
    }
}
