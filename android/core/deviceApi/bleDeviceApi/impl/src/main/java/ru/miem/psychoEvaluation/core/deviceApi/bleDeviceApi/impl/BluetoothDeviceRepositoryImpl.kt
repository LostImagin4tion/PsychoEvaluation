package ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.timeout
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.BluetoothDeviceRepository
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.models.BluetoothDeviceStatus
import timber.log.Timber
import java.security.InvalidParameterException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class BluetoothDeviceRepositoryImpl @Inject constructor() : BluetoothDeviceRepository {

    private val devicesScanCallback = object: ScanCallback() {
        var onScanResultCallback: (Int, ScanResult?) -> Unit = { _, _ -> }
        var onScanCompleted: () -> Unit = {}

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            onScanResultCallback(callbackType, result)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Timber.tag(TAG)
                    .e("Bluetooth LE Scan failed, reason: ${errorCode.toScanErrorMessage()}")
            }
        }
    }

    override val devicesFlow: Flow<BluetoothDevice> = createScanCallbackFlow()

    override val deviceDataFlow: Flow<Int>
        get() = TODO("Not yet implemented")

    override val isConnected: Boolean
        get() = TODO("Not yet implemented")

    @SuppressLint("MissingPermission")
    override fun scanForDevices(scanner: BluetoothLeScanner) {
        devicesScanCallback.onScanCompleted = { scanner.stopScan(devicesScanCallback) }
        scanner.startScan(devicesScanCallback)
    }

    override fun connectToBluetoothDevice() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    @OptIn(FlowPreview::class)
    private fun createScanCallbackFlow(): Flow<BluetoothDevice> = callbackFlow {
        devicesScanCallback.apply {
            onScanResultCallback = { callbackType, result ->
                result.toBluetoothDevice(callbackType)
                    ?.let {
                        trySendBlocking(it).onFailure {  throwable ->
                            Timber.tag(TAG).d("Failed to send new bluetooth device $it " +
                                    "with error $throwable ${throwable?.message}"
                            )
                        }
                    }
            }
        }

        awaitClose(devicesScanCallback.onScanCompleted)
    }
        .timeout(10.seconds)
        .catch { throwable ->
            val exceptionMessage = if (throwable is TimeoutCancellationException) {
                "Scan flow completed with timeout exception $throwable ${throwable.message}"
            } else {
                "Scan flow failed with exception $throwable ${throwable.message}"
            }
            Timber.tag(TAG).e(exceptionMessage)
        }
        .flowOn(Dispatchers.IO)

    @SuppressLint("MissingPermission")
    private fun ScanResult?.toBluetoothDevice(callbackType: Int) = this
        ?.takeIf { it.isConnectable }
        ?.device
        ?.let {
            BluetoothDevice(
                it.name,
                it.address,
                callbackType.callbackTypeToStatus()
            )
        }

    private fun Int.callbackTypeToStatus(): BluetoothDeviceStatus = when (this) {
        ScanSettings.CALLBACK_TYPE_FIRST_MATCH,
        ScanSettings.CALLBACK_TYPE_ALL_MATCHES,
        ScanSettings.CALLBACK_TYPE_ALL_MATCHES_AUTO_BATCH,
        -> BluetoothDeviceStatus.AVAILABLE

        ScanSettings.CALLBACK_TYPE_MATCH_LOST -> BluetoothDeviceStatus.NOT_AVAILABLE
        else -> throw InvalidParameterException("Got unexpected code of scan callback type: $this")
    }

    private companion object {
        val TAG: String = BluetoothDeviceRepositoryImpl::class.java.simpleName
    }
}
