package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.bluetooth.BluetoothManager
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothLeDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.ActivityInfo
import android.hardware.usb.UsbManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.soywiz.korge.android.KorgeAndroidView
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.designSystem.charts.SingleLineChart
import ru.miem.psychoEvaluation.common.designSystem.system.EnableBluetoothIfNeeded
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.designSystem.system.RequestBluetoothPermissionsIfNeeded
import ru.miem.psychoEvaluation.common.designSystem.system.SystemBroadcastReceiver
import ru.miem.psychoEvaluation.common.designSystem.system.requestPermissionIntentAction
import ru.miem.psychoEvaluation.common.designSystem.system.requestUsbDeviceAccess
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.GameModule
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

class AirplaneGameScreenImpl @Inject constructor() : AirplaneGameScreen {

    @Composable
    override fun AirplaneGameScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
            .adapter

        val viewModel: AirplaneGameScreenViewModel = viewModel()

        var isDeviceAccessGranted by remember {
            mutableStateOf(viewModel.isUsbDeviceAccessGranted(usbManager))
        }
        var isBluetoothAccessGranted by remember {
            mutableStateOf(false)
        }

        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        RequestBluetoothPermissionsIfNeeded { permissionsGranted ->
            isBluetoothAccessGranted = permissionsGranted
        }

        if (isBluetoothAccessGranted) {
            EnableBluetoothIfNeeded(bluetoothAdapter)
        }

        // TODO listen for bluetooth state via broadcast receiver
        //  https://developer.android.com/develop/connectivity/bluetooth/setup#kotlin

        SystemBroadcastReceiver(
            isExported = true,
            systemAction = context.requestPermissionIntentAction,
            onSystemEvent = { _ ->
                if (viewModel.isUsbDeviceAccessGranted(usbManager)) {
                    isDeviceAccessGranted = true
                }
            }
        )

        LaunchedEffect(isDeviceAccessGranted) {
            if (viewModel.hasConnectedDevices(usbManager)) {
                if (!isDeviceAccessGranted) {
                    usbManager.requestUsbDeviceAccess(context)
                } else {
                    viewModel.connectToUsbDevice(
                        usbManager = usbManager,
                        screenHeight = context.resources.displayMetrics.heightPixels.toDouble()
                    )
                }
            }
        }

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
            }
        }

        AirplaneGameScreenContent(
            dataFlow = viewModel.stressData,
            modelProducer = viewModel.chartModelProducer,
        )
    }

    @Composable
    private fun AirplaneGameScreenContent(
        dataFlow: StateFlow<UsbDeviceData>,
        modelProducer: CartesianChartModelProducer,
    ) = Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                val displayMetrics = context.resources.displayMetrics
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels
                Timber.tag(TAG).d("Setting height $height and width  $width")

                KorgeAndroidView(context).apply {
                    loadModule(
                        GameModule(
                            screenWidth = width,
                            screenHeight = height,
                            context = context,
                            dataFlow = dataFlow,
                        )
                    )
                }
            }
        )

        SingleLineChart(
            modelProducer = modelProducer,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 8.dp)
                )
                .size(width = 200.dp, height = 150.dp)
                .padding(8.dp)
        )
    }

    private companion object {
        val TAG: String = AirplaneGameScreenImpl::class.java.simpleName
    }
}
