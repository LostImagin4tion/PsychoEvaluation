package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService

sealed interface DeviceDelegate {
    fun connectCharacteristics(gattService: BluetoothGattService): Boolean = true

    // following methods only overwritten for Telit devices
    fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {}

    fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {}

    fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {}

    fun canWrite(): Boolean = true

    fun disconnect() {}
}
