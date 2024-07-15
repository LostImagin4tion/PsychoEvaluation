package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.usb.UsbManager
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
import androidx.compose.runtime.collectAsState
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
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.GameModule
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import timber.log.Timber
import javax.inject.Inject

class AirplaneGameScreenImpl @Inject constructor() : AirplaneGameScreen {

    @Composable
    override fun AirplaneGameScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

        val viewModel: AirplaneGameScreenViewModel = viewModel()

        val sensorDeviceType = viewModel.sensorDeviceType.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        when (sensorDeviceType.value) {
            SensorDeviceType.USB -> {
                viewModel.connectToUsbDevice(
                    usbManager = usbManager,
                    screenHeight = context.resources.displayMetrics.heightPixels.toDouble(),
                )
            }
            SensorDeviceType.BLUETOOTH -> {
                viewModel.retrieveDataFromBluetoothDevice(
                    screenHeight = context.resources.displayMetrics.heightPixels.toDouble(),
                )
            }
            SensorDeviceType.UNKNOWN -> {}
        }

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
            }
        }

        AirplaneGameScreenContent(
            showMessage = showMessage,
            dataFlow = viewModel.stressData,
            modelProducer = viewModel.chartModelProducer,
        )
    }

    @Composable
    private fun AirplaneGameScreenContent(
        showMessage: (String) -> Unit,
        dataFlow: StateFlow<SensorData>,
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
