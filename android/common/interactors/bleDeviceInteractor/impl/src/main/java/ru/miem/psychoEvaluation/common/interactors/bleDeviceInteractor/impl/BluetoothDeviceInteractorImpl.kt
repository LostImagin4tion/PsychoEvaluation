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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.toList
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDevice
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.BluetoothDeviceData
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.callbackTypeToStatus
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.models.toBluetoothDevice
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl.utils.toScanErrorMessage
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.Borders
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.di.DataAnalysisDiApi
import ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api.di.BluetoothDeviceRepositoryDiApi
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.core.utils.coroutines.withIO
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class BluetoothDeviceInteractorImpl @Inject constructor() : BluetoothDeviceInteractor {

    private val bleDeviceRepository by diApi(BluetoothDeviceRepositoryDiApi::bluetoothDeviceRepository)
    private val airplaneGameDataAnalysis by diApi(DataAnalysisDiApi::dataAnalysis)

    private val devicesScanCallback = object : ScanCallback() {
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

    private var dataBorders: Borders? = null

    override val devicesFlow: Flow<BluetoothDevice> = createScanCallbackFlow()

    override suspend fun findDataBorders(onCompleted: () -> Unit) {
        withIO {
            val preparationData = airplaneGameDataAnalysis.findPreparationData(
                bleDeviceRepository.deviceDataFlow
            )

            dataBorders = preparationData.toList()
                .let {
                    airplaneGameDataAnalysis.findDataBorders(it)
                }
                .also {
                    onCompleted()
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

    override suspend fun getDeviceData(
        onNewValueEmitted: suspend (BluetoothDeviceData) -> Unit
    ) {
        withIO {
            bleDeviceRepository.deviceDataFlow.collect { rawData ->
                val borders = dataBorders
                check(borders != null) {
                    "Data borders is null. You must detect it before collecting sensor data"
                }

                onNewValueEmitted(
                    BluetoothDeviceData(
                        rawData = rawData,
                        normalizedData = airplaneGameDataAnalysis.getNormalizedValue(
                            value = rawData.toDouble(),
                            borders = borders,
                        ),
                        upperLimit = borders.upperLimit,
                        lowerLimit = borders.lowerLimit,
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
        activity = activity,
        bluetoothAdapter = bluetoothAdapter,
        deviceHardwareAddress = deviceHardwareAddress,
        onDeviceConnected = onDeviceConnected
    )

    override fun disconnect() {
        bleDeviceRepository.disconnect()
    }

    override fun increaseGameDifficulty() {
        dataBorders = dataBorders?.let { airplaneGameDataAnalysis.increaseDifficulty(it) }
    }

    override fun decreaseGameDifficulty() {
        dataBorders = dataBorders?.let { airplaneGameDataAnalysis.decreaseDifficulty(it) }
    }

    override fun changeDataBorders(upperLimit: Double?, lowerLimit: Double?) {
        dataBorders = dataBorders?.run {
            copy(
                upperLimit = upperLimit
                    ?.let { 1 + it }
                    ?: this.upperLimit,
                lowerLimit = lowerLimit
                    ?.let { 1 - it }
                    ?: this.lowerLimit
            )
        }
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
                                        "with callback type %s with error %s",
                                    device,
                                    callbackType.toString(),
                                    throwable.toString(),
                                )
                        }
                    }
            }
        }

        awaitClose(devicesScanCallback.onScanCompleted)
    }
        .timeout(30.seconds)
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
