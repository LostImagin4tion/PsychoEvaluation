package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.SerialListener
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates.Cc245XDelegate
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates.DeviceDelegate
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates.MicrochipDelegate
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.deviceDelegates.NrfDelegate
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.utils.BleDevices
import timber.log.Timber
import java.io.IOException
import java.util.Arrays
import kotlin.math.min

/**
 * wrap BLE communication into socket like class
 * - connect, disconnect and write as methods,
 * - read + status is returned by SerialListener
 */
@SuppressLint("MissingPermission")
class SerialSocket(
    private val context: Context,
    private var device: BluetoothDevice?
) : BluetoothGattCallback() {

    private val writeBuffer: ArrayList<ByteArray> = ArrayList()

    private var listener: SerialListener? = null
    private var delegate: DeviceDelegate? = null
    private var gatt: BluetoothGatt? = null
    private var readCharacteristic: BluetoothGattCharacteristic? = null
    private var writeCharacteristic: BluetoothGattCharacteristic? = null

    private var writePending = false
    private var canceled = false
    private var connected = false
    private var payloadSize = DEFAULT_MTU - MTU_NUM

    fun disconnect() {
        Timber.tag(TAG).d("disconnect()")

        listener = null // ignore remaining data and errors
        device = null
        canceled = true

        synchronized(writeBuffer) {
            writePending = false
            writeBuffer.clear()
        }

        readCharacteristic = null
        writeCharacteristic = null

        delegate?.disconnect()

        if (gatt != null) {
            Timber.tag(TAG).d("GATT disconnect")
            gatt?.disconnect()

            Timber.tag(TAG).d("GATT close")
            gatt?.close()

            gatt = null
            connected = false
        }
    }

    /**
     * connect-success and most connect-errors are returned asynchronously to listener
     */
    @Throws(IOException::class)
    fun connect(listener: SerialListener?) {
        Timber.tag(TAG).d("connect(SerialListener?)")
        if (connected || gatt != null) {
            throw IOException("Already connected")
        }

        canceled = false
        this.listener = listener

        Timber.tag(TAG).d("Connected $device")
        Timber.tag(TAG).d("Connect GATT, LE")

        gatt = device?.connectGatt(
            context,
            false,
            this,
            BluetoothDevice.TRANSPORT_LE
        )

        if (gatt == null) {
            throw IOException("ConnectGatt failed")
        }
        // continues asynchronously in onPairingBroadcastReceive() and onConnectionStateChange()
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        Timber.tag(TAG).d("onConnectionStateChange(BluetoothGatt, Int, Int)")
        // status directly taken from gat_api.h, e.g. 133=0x85=GATT_ERROR ~= timeout
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Timber.tag(TAG).d("Connect status $status, discoverServices")

            if (!gatt.discoverServices()) {
                onSerialConnectError(IOException("Discover services failed"))
            }
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            if (connected) {
                onSerialIoError(IOException("GATT status $status"))
            } else {
                onSerialConnectError(IOException("GATT status $status"))
            }
        } else {
            Timber.tag(TAG).d("Unknown connect state $newState $status")
        }
        // continues asynchronously in onServicesDiscovered()
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        Timber.tag(TAG).d("onServicesDiscovered(BluetoothGatt, Int)")
        Timber.tag(TAG).d("Services discovered, status $status")
        if (canceled) {
            return
        }
        connectCharacteristics1(gatt)
    }

    private fun connectCharacteristics1(gatt: BluetoothGatt) {
        Timber.tag(TAG).d("connectCharacteristics1(BluetoothGatt)")
        var sync = true
        writePending = false
        for (gattService in gatt.services) {
            val onCharacteristicsCreated: (
                BluetoothGattCharacteristic,
                BluetoothGattCharacteristic
            ) -> Unit = { read, write ->
                readCharacteristic = read
                writeCharacteristic = write
            }

            delegate = when (gattService.uuid) {
                BleDevices.BLUETOOTH_LE_CC254X_SERVICE -> Cc245XDelegate(onCharacteristicsCreated)
                BleDevices.BLUETOOTH_LE_MICROCHIP_SERVICE -> MicrochipDelegate(onCharacteristicsCreated)
                BleDevices.BLUETOOTH_LE_NRF_SERVICE -> NrfDelegate(
                    onCharacteristicsCreated,
                    ::onSerialConnectError
                )
                else -> null
            }

            delegate?.let {
                sync = it.connectCharacteristics(gattService)
            }
            if (delegate != null) {
                break
            }
        }

        if (canceled) {
            return
        }

        if (delegate == null || readCharacteristic == null || writeCharacteristic == null) {
            for (gattService in gatt.services) {
                Timber.tag(TAG).d("Service ${gattService.uuid}")

                for (characteristic in gattService.characteristics) {
                    Timber.tag(TAG).d("Characteristic ${characteristic.uuid}")
                }
            }
            onSerialConnectError(IOException("No serial profile found"))
            return
        }
        if (sync) {
            connectCharacteristics2(gatt)
        }
    }

    private fun connectCharacteristics2(gatt: BluetoothGatt) {
        Timber.tag(TAG).d("connectCharacteristics2(BluetoothGatt)")
        Timber.tag(TAG).d("Request max MTU")
        if (!gatt.requestMtu(MAX_MTU)) {
            onSerialConnectError(IOException("Request MTU failed"))
        }
        // continues asynchronously in onMtuChanged
    }

    override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
        Timber.tag(TAG).d("onMtuChanged(BluetoothGatt, Int, Int)")
        Timber.tag(TAG).d("MTU size $mtu, status=$status")

        if (status == BluetoothGatt.GATT_SUCCESS) {
            payloadSize = mtu - MTU_NUM
            Timber.tag(TAG).d("Payload size $payloadSize")
        }
        connectCharacteristics3(gatt)
    }

    private fun connectCharacteristics3(gatt: BluetoothGatt) {
        Timber.tag(TAG).d("connectCharacteristics3(BluetoothGatt)")
        writeCharacteristic?.properties
            ?.let {
                val secondOperand = BluetoothGattCharacteristic.PROPERTY_WRITE +
                    BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE

                if (it and secondOperand == 0) { // HM10,TI uart,Telit have only WRITE_NO_RESPONSE
                    onSerialConnectError(IOException("Write characteristic not writable"))
                    return
                }
            }

        if (!gatt.setCharacteristicNotification(readCharacteristic, true)) {
            onSerialConnectError(IOException("No notification for read characteristic"))
            return
        }

        val readDescriptor = readCharacteristic?.getDescriptor(BleDevices.BLUETOOTH_LE_CCCD)

        if (readDescriptor == null) {
            onSerialConnectError(IOException("No CCCD descriptor for read characteristic"))
            return
        }

        readCharacteristic?.properties
            ?.let {
                if (it and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                    val result = writeDescriptor(
                        gatt,
                        readDescriptor,
                        BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                    )
                    Timber.tag(TAG).d("Enable read indication result=$result")

                    if (!result) {
                        onSerialConnectError(IOException("Read characteristic CCCD descriptor not writable"))
                    }
                } else if (it and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                    val result = writeDescriptor(
                        gatt,
                        readDescriptor,
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    )
                    Timber.tag(TAG).d("Enable read notification result=$result")

                    if (!result) {
                        onSerialConnectError(IOException("Read characteristic CCCD descriptor not writable"))
                    }
                } else {
                    onSerialConnectError(
                        IOException("No indication/notification for read characteristic ($it)")
                    )
                    return
                }
            }
        // continues asynchronously in onDescriptorWrite()
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {
        Timber.tag(TAG).d("onDescriptorWrite(BluetoothGatt, BluetoothGattDescriptor, Int)")
        delegate?.onDescriptorWrite(gatt, descriptor, status)

        if (canceled) {
            return
        }

        if (descriptor.characteristic === readCharacteristic) {
            Timber.tag(TAG)
                .d("Writing read characteristic descriptor finished, status=$status")

            if (status != BluetoothGatt.GATT_SUCCESS) {
                onSerialConnectError(IOException("Write descriptor failed"))
            } else {
                // onCharacteristicChanged with incoming data
                // can happen after writeDescriptor(ENABLE_INDICATION/NOTIFICATION)
                // before confirmed by this method,
                // so receive data can be shown before device is shown as 'Connected'.
                onSerialConnect()
                connected = true
                Timber.tag(TAG).d("Connected")
            }
        }
    }

    /*
     * read
     */
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ) {
        Timber.tag(TAG)
            .d("onCharacteristicChanged(BluetoothGatt, BluetoothGattCharacteristic, Int)")
        super.onCharacteristicChanged(gatt, characteristic, value)
        if (canceled) {
            return
        }
        delegate?.onCharacteristicChanged(gatt, characteristic)

        if (canceled) {
            return
        }

        readCharacteristic?.let { read ->
            if (characteristic === read) { // NOPMD - test object identity
                onSerialRead(value)
                Timber.tag(TAG).d("read, len=${value.size}")
            }
        }
    }

    /*
     * write
     */
    @Throws(IOException::class)
    fun write(data: ByteArray) {
        Timber.tag(TAG).d("write(ByteArray)")
        val gatt = this.gatt
        val writeCharacteristic = this.writeCharacteristic

        if (canceled || !connected || gatt == null || writeCharacteristic == null) {
            throw IOException("Not connected")
        }

        var data0: ByteArray?

        synchronized(writeBuffer) {
            data0 = if (data.size <= payloadSize) {
                data
            } else {
                Arrays.copyOfRange(data, 0, payloadSize)
            }

            if (!writePending && writeBuffer.isEmpty() && delegate?.canWrite() == true) {
                writePending = true
            } else {
                data0?.let {
                    writeBuffer.add(it)
                }
                Timber.tag(TAG).d("Write queued, len=${data0?.size}")
                data0 = null
            }

            if (data.size > payloadSize) {
                for (i in 1 until (data.size + payloadSize - 1) / payloadSize) {
                    val from = i * payloadSize
                    val to = min(
                        (from + payloadSize).toDouble(),
                        data.size.toDouble()
                    ).toInt()
                    writeBuffer.add(Arrays.copyOfRange(data, from, to))
                    Timber.tag(TAG).d("Write queued, len=${to - from}")
                }
            }
        }

        data0?.let {
            if (!writeCharacteristic(gatt, writeCharacteristic, it)) {
                onSerialIoError(IOException("Write failed"))
            } else {
                Timber.tag(TAG).d("Write started, len=${data0?.size}")
            }
        }
        // continues asynchronously in onCharacteristicWrite()
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        Timber.tag(TAG)
            .d("onCharacteristicWrite(BluetoothGatt, BluetoothGattCharacteristic, Int)")
        if (canceled || !connected || writeCharacteristic == null) {
            return
        }

        if (status != BluetoothGatt.GATT_SUCCESS) {
            onSerialIoError(IOException("Write failed"))
            return
        }

        delegate?.onCharacteristicWrite(gatt, characteristic, status)

        if (canceled) {
            return
        }
        if (characteristic === writeCharacteristic) { // NOPMD - test object identity
            Timber.tag(TAG).d("Write finished, status=$status")
            writeNext()
        }
    }

    private fun writeNext() {
        Timber.tag(TAG).d("writeNext()")
        val gatt = this.gatt
        val writeCharacteristic = this.writeCharacteristic

        if (gatt == null || writeCharacteristic == null) {
            throw IOException("Not connected")
        }
        val data: ByteArray?

        synchronized(writeBuffer) {
            if (writeBuffer.isNotEmpty() && delegate?.canWrite() == true) {
                writePending = true
                data = writeBuffer.removeAt(0)
            } else {
                writePending = false
                data = null
            }
        }

        data?.let {
            if (!writeCharacteristic(gatt, writeCharacteristic, it)) {
                onSerialIoError(IOException("Write failed"))
            } else {
                Timber.tag(TAG).d("Write started, len=${data.size}")
            }
        }
    }

    private fun onSerialConnect() {
        listener?.onSerialConnect()
    }

    private fun onSerialConnectError(e: Exception) {
        canceled = true
        listener?.onSerialConnectError(e)
    }

    private fun onSerialRead(data: ByteArray) {
        listener?.onSerialRead(data)
    }

    private fun onSerialIoError(e: Exception) {
        writePending = false
        canceled = true
        listener?.onSerialIoError(e)
    }

    private fun writeDescriptor(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        value: ByteArray
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeDescriptor(descriptor, value) == BluetoothStatusCodes.SUCCESS
        } else {
            descriptor.setValue(value)
            gatt.writeDescriptor(descriptor)
        }
    }

    private fun writeCharacteristic(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeCharacteristic(characteristic, value, characteristic.writeType) == BluetoothStatusCodes.SUCCESS
        } else {
            characteristic.setValue(value)
            gatt.writeCharacteristic(characteristic)
        }
    }

    companion object {
        // BLE standard does not limit, some BLE 4.2 devices support 251,
        // various source say that Android has max 512
        private const val MAX_MTU = 512
        private const val DEFAULT_MTU = 23
        private const val MTU_NUM = 3

        private val TAG: String = SerialSocket::class.java.simpleName
    }
}
