package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.SerialListener
import java.io.IOException

/**
 * queue serial data while activity is not in the foreground
 * use listener chain: SerialSocket -> SerialService -> UI fragment
 */
class SerialService : Service(), SerialListener {

    inner class SerialBinder : Binder() {
        val service: SerialService
            get() = this@SerialService
    }

    private enum class QueueType {
        Connect,
        ConnectError,
        Read,
        IoError
    }

    private class QueueItem(
        var type: QueueType
    ) {
        var data: ByteArray = byteArrayOf()
        var exception: Exception? = null

        constructor(type: QueueType, exception: Exception?) : this(type) {
            this.exception = exception
        }

        constructor(type: QueueType, data: ByteArray) : this(type) {
            this.data = data
        }

        fun add(data: ByteArray) {
            this.data = data
        }
    }

    private val mainLooper = Handler(Looper.getMainLooper())
    private val binder: IBinder = SerialBinder()
    private val queue1: ArrayDeque<QueueItem> = ArrayDeque()
    private val queue2: ArrayDeque<QueueItem> = ArrayDeque()
    private val lastRead: QueueItem = QueueItem(QueueType.Read)

    private var socket: SerialSocket? = null
    private var listener: SerialListener? = null
    private var connected = false

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder = binder

    /**
     * SerialListener
     */
    override fun onSerialConnect() {
        if (connected) {
            synchronized(this) {
                listener?.let {
                    mainLooper.post {
                        listener?.onSerialConnect() ?: queue1.add(QueueItem(QueueType.Connect))
                    }
                } ?: queue2.add(QueueItem(QueueType.Connect))
            }
        }
    }

    override fun onSerialConnectError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                listener?.let {
                    mainLooper.post {
                        listener?.onSerialIoError(e)
                            ?: run {
                                queue1.add(QueueItem(QueueType.ConnectError, e))
                                disconnect()
                            }
                    }
                } ?: run {
                    queue2.add(QueueItem(QueueType.ConnectError, e))
                    disconnect()
                }
            }
        }
    }

    /**
     * reduce number of UI updates by merging data chunks.
     * Data can arrive at hundred chunks per second, but the UI can only
     * perform a dozen updates if receiveText already contains much text.
     *
     * On new data inform UI thread once (1).
     * While not consumed (2), add more data (3).
     */
    override fun onSerialRead(data: ByteArray) {
        if (connected) {
            synchronized(this) {
                listener?.let {
                    var first: Boolean

                    synchronized(lastRead) {
                        first = lastRead.data.isEmpty() == true // (1)
                        lastRead.add(data) // (3)
                    }

                    if (first) {
                        mainLooper.post {
                            var newData: ByteArray

                            synchronized(lastRead) {
                                newData = lastRead.data
                            }

                            this.listener?.onSerialRead(newData)
                                ?: queue1.add(QueueItem(QueueType.Read, newData))
                        }
                    }
                } ?: run {
                    if (queue2.isEmpty() || queue2.last().type != QueueType.Read) {
                        queue2.add(QueueItem(QueueType.Read))
                    }
                    queue2.last().add(data)
                }
            }
        }
    }

    override fun onSerialIoError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                listener?.let {
                    mainLooper.post {
                        listener?.onSerialIoError(e)
                            ?: run {
                                queue1.add(QueueItem(QueueType.IoError, e))
                                disconnect()
                            }
                    }
                } ?: run {
                    queue2.add(QueueItem(QueueType.IoError, e))
                    disconnect()
                }
            }
        }
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?) {
        if (!connected) {
            throw IOException("Not connected")
        }
        data?.let {
            socket?.write(it)
        }
    }

    /**
     * Api
     */
    @Throws(IOException::class)
    fun connect(socket: SerialSocket) {
        socket.connect(listener)
        this.socket = socket
        connected = true
    }

    fun disconnect() {
        connected = false // ignore data, errors while disconnecting
        socket?.disconnect()
        socket = null
    }

    fun attach(listener: SerialListener) {
        require(Looper.getMainLooper().thread === Thread.currentThread()) {
            "Not in main thread"
        }

        // use synchronized() to prevent new items in queue2
        // new items will not be added to queue1 because mainLooper.post and attach() run in main thread
        synchronized(this) {
            this.listener = listener
        }

        for (item in queue1) {
            when (item.type) {
                QueueType.Connect -> listener.onSerialConnect()
                QueueType.ConnectError -> listener.onSerialConnectError(item.exception)
                QueueType.Read -> listener.onSerialRead(item.data)
                QueueType.IoError -> listener.onSerialIoError(item.exception)
            }
        }

        for (item in queue2) {
            when (item.type) {
                QueueType.Connect -> listener.onSerialConnect()
                QueueType.ConnectError -> listener.onSerialConnectError(item.exception)
                QueueType.Read -> listener.onSerialRead(item.data)
                QueueType.IoError -> listener.onSerialIoError(item.exception)
            }
        }

        queue1.clear()
        queue2.clear()
    }

    fun detach() {
        // items already in event queue (posted before detach() to mainLooper) will end up in queue1
        // items occurring later, will be moved directly to queue2
        // detach() and mainLooper.post run in the main thread, so all items are caught
        listener = null
    }
}
