package ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl

import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api.UsbDeviceRepository
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class UsbDeviceRepositoryImpl @Inject constructor() : UsbDeviceRepository {

    private var usbIOManager: SerialInputOutputManager? = null
    private var usbSerialPort: UsbSerialPort? = null

    private val usbDeviceDataFlow = createCallbackFlow()

    private val usbDeviceListener = object : SerialInputOutputManager.Listener {
        var onNewDataCallback: (ByteArray?) -> Unit = {}
        var onRunErrorCallback: (Exception?) -> Unit = {}

        override fun onNewData(data: ByteArray?) = onNewDataCallback(data)
        override fun onRunError(e: Exception?) = onRunErrorCallback(e)
    }

    override val deviceDataFlow: Flow<Int> = usbDeviceDataFlow

    override var isConnected: Boolean = false
        private set

    override fun connectToUsbDevice(
        usbManager: UsbManager,
        portIndex: Int,
        baudRate: Int,
        useIOManager: Boolean,
    ) {
        val device = usbManager.deviceList.values.lastOrNull()

        if (device == null) {
            Timber.tag(TAG).e("Connection failed: device not found")
            return
        }

        val driver = UsbSerialProber.getDefaultProber().probeDevice(device)
            ?: CustomProber.customProber.probeDevice(device)

        if (driver == null) {
            Timber.tag(TAG).e("Connection failed: didn't find driver  for device")
            return
        }

        if (driver.ports.size < portIndex) {
            Timber.tag(TAG).e("Connection failed: not enough ports at device")
            return
        }

        usbSerialPort = driver.ports[portIndex]

        val usbConnection = usbManager.openDevice(driver.device)
        if (usbConnection == null) {
            if (!usbManager.hasPermission(driver.device)) {
                Timber.tag(TAG).e("Connection failed: permission denied")
            } else {
                Timber.tag(TAG).e("Connection failed: open failed")
            }
            return
        }

        usbSerialPort?.let { port ->
            port.open(usbConnection)

            try {
                port.setParameters(baudRate, DATA_BITS, STOP_BITS, UsbSerialPort.PARITY_NONE)
            } catch (e: UnsupportedOperationException) {
                Timber.tag(TAG).e("setParameters() not supported")
            }

            if (useIOManager) {
                usbIOManager = SerialInputOutputManager(usbSerialPort, usbDeviceListener)
                    .apply { start() }
            }

            Timber.tag(TAG).i("Connected successfully")
            isConnected = true
        } ?: Timber.tag(TAG).e("Connection failed somehow")
    }

    override fun disconnect() {
        isConnected = false

        usbIOManager?.apply {
            listener = null
            stop()
        }
        usbIOManager = null

        usbSerialPort
            ?.takeIf { it.isOpen }
            ?.close()
        usbSerialPort = null
    }

    private fun createCallbackFlow(): Flow<Int> = callbackFlow {
        usbDeviceListener.apply {
            onNewDataCallback = { data ->
                data.toUsbDeviceInt()?.let {
                    trySendBlocking(it).onFailure { throwable ->
                        Timber.tag(TAG).d("Failed to send new stress data $throwable ${throwable?.message}")
                    }
                }
            }
            onRunErrorCallback = { e ->
                disconnect()
                close(e)
            }
        }

        awaitClose()
    }
        .catch { e ->
            Timber.tag(TAG).e("Lost connection with device: message ${e.message}")
        }
        .flowOn(Dispatchers.IO)

    private fun ByteArray?.toUsbDeviceInt(): Int? {
        return this
            .takeIf { it?.isNotEmpty() == true }
            ?.let { data ->
                val string = String(data, StandardCharsets.UTF_8)
                string.substring(2, string.lastIndex).toInt(INT_RADIX)
            }
            ?.takeIf { it != 0 }
    }

    private companion object {
        val TAG: String = UsbDeviceRepository::class.java.simpleName

        const val DATA_BITS = 8
        const val STOP_BITS = 1

        const val INT_RADIX = 16
    }
}
