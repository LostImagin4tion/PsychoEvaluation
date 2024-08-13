package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
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
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.soywiz.korge.android.KorgeAndroidView
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.designSystem.charts.SingleLineChart
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.common.designSystem.utils.findActivity
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingScreenArgs
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.GameModule
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import timber.log.Timber
import javax.inject.Inject

class AirplaneGameScreenImpl @Inject constructor() : AirplaneGameScreen {

    @Composable
    override fun AirplaneGameScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingScreenArgs: TrainingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val viewModel: AirplaneGameScreenViewModel = viewModel(
            factory = viewModelFactory {
                AirplaneGameScreenViewModel(
                    usbDeviceInteractor,
                    bleDeviceInteractor,
                )
            }
        )

        val sensorDeviceType = viewModel.sensorDeviceType.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        when (sensorDeviceType.value) {
            SensorDeviceType.Usb -> {
                viewModel.connectToUsbDevice(usbManager = usbManager)
            }
            SensorDeviceType.Bluetooth -> {
                val activity = context.findActivity()
                val deviceHardwareAddress = trainingScreenArgs.bleDeviceHardwareAddress

                require(activity != null && deviceHardwareAddress != null) {
                    "Activity $activity and deviceHardwareAddress $deviceHardwareAddress cant be null"
                }

                viewModel.retrieveDataFromBluetoothDevice(
                    activity = activity,
                    bluetoothAdapter = bluetoothManager.adapter,
                    bleDeviceHardwareAddress = deviceHardwareAddress,
                )
            }
            SensorDeviceType.Unknown -> {}
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
            increaseGameDifficulty = viewModel::increaseGameDifficulty,
            decreaseGameDifficulty = viewModel::decreaseGameDifficulty
        )
    }

    @Composable
    private fun AirplaneGameScreenContent(
        showMessage: (String) -> Unit,
        dataFlow: StateFlow<SensorData>,
        modelProducer: CartesianChartModelProducer,
        increaseGameDifficulty: () -> Unit,
        decreaseGameDifficulty: () -> Unit,
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
                            increaseGameDifficulty = increaseGameDifficulty,
                            decreaseGameDifficulty = decreaseGameDifficulty,
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
