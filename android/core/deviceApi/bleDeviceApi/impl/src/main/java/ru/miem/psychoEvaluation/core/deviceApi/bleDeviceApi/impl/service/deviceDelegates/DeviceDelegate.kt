package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService

open class DeviceDelegate {
    open fun connectCharacteristics(gattService: BluetoothGattService): Boolean = true

    // following methods only overwritten for Telit devices
    open fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {}

    open fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {}

    open fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {}

    open fun canWrite(): Boolean = true

    open fun disconnect() {}
}
