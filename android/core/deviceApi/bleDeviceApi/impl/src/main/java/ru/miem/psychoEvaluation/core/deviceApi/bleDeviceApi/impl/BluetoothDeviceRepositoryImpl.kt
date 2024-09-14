package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.BluetoothDeviceRepository
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.SerialListener
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.SerialService
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service.SerialSocket
import timber.log.Timber
import javax.inject.Inject

class BluetoothDeviceRepositoryImpl @Inject constructor() :
    BluetoothDeviceRepository,
    ServiceConnection {

    private val serialListener = object : SerialListener {
        var onSerialReadCallback: (ByteArray) -> Unit = {}

        override fun onSerialConnect() {
            isConnected = true
            onDeviceConnected()
        }

        override fun onSerialConnectError(e: Exception?) {
            disconnect()
        }

        override fun onSerialRead(data: ByteArray) {
            onSerialReadCallback(data)
        }

        override fun onSerialIoError(e: Exception?) {
            disconnect()
        }
    }

    private var serialService: SerialService? = null
    private var context: Context? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var deviceAddress: String? = null
    private var onDeviceConnected: () -> Unit = {}

    override val deviceDataFlow: Flow<Int>
        get() = createSerialListenerCallbackFlow()

    override var isConnected: Boolean = false
        private set

    override fun connectToBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        deviceHardwareAddress: String,
        onDeviceConnected: () -> Unit,
    ) {
        this.context = activity
        this.bluetoothAdapter = bluetoothAdapter
        this.deviceAddress = deviceHardwareAddress
        this.onDeviceConnected = onDeviceConnected

        serialService
            ?.apply {
                attach(serialListener)
                connectToSerialSocket()
            }
            ?: run {
                activity.bindService(
                    Intent(activity, SerialService::class.java),
                    this,
                    Context.BIND_AUTO_CREATE
                )
            }
    }

    override fun disconnect() {
        serialService?.disconnect()
        isConnected = false
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Timber.tag(TAG).d("onServiceConnected(ComponentName?, IBinder?)")
        serialService = (service as SerialService.SerialBinder)
            .service
            .apply {
                attach(serialListener)
            }

        connectToSerialSocket()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        serialService = null
    }

    private fun connectToSerialSocket() {
        val context = this.context
        val bluetoothAdapter = this.bluetoothAdapter
        val deviceAddress = this.deviceAddress

        if (context != null && bluetoothAdapter != null && deviceAddress != null) {
            val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
            val socket = SerialSocket(context)

            serialService?.connect(device, socket)
        }
    }

    private fun createSerialListenerCallbackFlow(): Flow<Int> = callbackFlow {
        serialListener.apply {
            onSerialReadCallback = { bytes ->
                val stressData = convertData(bytes)

                trySendBlocking(stressData).onFailure { throwable ->
                    Timber.tag(TAG).e(
                        message = "Failed to send new bytes from bluetooth device %s with error %s, %s",
                        bytes.joinToString(","),
                        throwable.toString(),
                        throwable?.message
                    )
                }
            }
        }

        awaitClose {
            disconnect()
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalStdlibApi::class)
    private fun convertData(data: ByteArray): Int {
        val stringBuilder = StringBuilder().apply {
            append(String(data))
        }

        return stringBuilder.removePrefix("M")
            .toString()
            .trim()
            .hexToInt(HexFormat.UpperCase)
    }

    private companion object {
        val TAG: String = BluetoothDeviceRepositoryImpl::class.java.simpleName
    }
}
