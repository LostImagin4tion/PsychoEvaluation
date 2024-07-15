package ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.timeout
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDeviceData
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.callbackTypeToStatus
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.toBluetoothDevice
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.utils.toScanErrorMessage
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.di.BluetoothDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.core.utils.coroutines.withIO
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class BluetoothDeviceInteractorImpl @Inject constructor() : BluetoothDeviceInteractor {

    private val bleDeviceRepository by diApi(BluetoothDeviceRepositoryDiApi::bluetoothDeviceRepository)

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

//    override val devicesDataFlow: Flow<Int> = bleDeviceRepository.deviceDataFlow

    override suspend fun getAllRawDeviceData(
        onNewValueEmitted: suspend (List<Int>) -> Unit
    ) {
        val bleDeviceData = mutableListOf<Int>()

        withIO {
            bleDeviceRepository.deviceDataFlow.collect { sensorData ->
                bleDeviceData.add(sensorData)
                onNewValueEmitted(bleDeviceData)
            }
        }
    }

    override suspend fun getRawDeviceData(
        onNewValueEmitted: suspend (Int) -> Unit
    ) {
        withIO {
            bleDeviceRepository.deviceDataFlow.collect(onNewValueEmitted)
        }
    }

    override suspend fun getNormalizedDeviceData(
        normalizationFactor: Double,
        onNewValueEmitted: suspend (BluetoothDeviceData) -> Unit
    ) {
        withIO {
            bleDeviceRepository.deviceDataFlow.collect { rawData ->
                onNewValueEmitted(
                    BluetoothDeviceData(
                        rawData,
                        rawData / normalizationFactor
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun scanForDevices(bluetoothScanner: BluetoothLeScanner) {
        devicesScanCallback.onScanCompleted = {
            bluetoothScanner.stopScan(devicesScanCallback)
        }
        bluetoothScanner.startScan(devicesScanCallback)
    }

    override fun connectToBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        deviceHardwareAddress: String,
        onDeviceConnected: () -> Unit,
    ) = bleDeviceRepository.connectToBluetoothDevice(
        activity,
        bluetoothAdapter,
        deviceHardwareAddress,
        onDeviceConnected
    )

    override fun disconnect() {
        bleDeviceRepository.disconnect()
    }

    @OptIn(FlowPreview::class)
    private fun createScanCallbackFlow(): Flow<BluetoothDevice> = callbackFlow {
        devicesScanCallback.apply {
            onScanResultCallback = { callbackType, result ->
                val deviceStatus = callbackType.callbackTypeToStatus()
                if (deviceStatus == null) {
                    Timber.tag(TAG)
                        .e("Got unexpected code of scan callback type: $callbackType")
                }

                result.toBluetoothDevice(deviceStatus)
                    ?.let {
                        trySendBlocking(it).onFailure { throwable ->
                            val device = result?.device
                            Timber.tag(TAG)
                                .e(
                                    message = "Failed to send new scan result %s " +
                                        "with callback type %s with error %s, %s",
                                    device,
                                    callbackType.toString(),
                                    throwable.toString(),
                                    throwable?.message
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

    private companion object {
        val TAG: String = BluetoothDeviceInteractorImpl::class.java.simpleName
    }
}