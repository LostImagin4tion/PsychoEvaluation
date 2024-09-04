package ru.miem.psychoEvaluation.feature.trainingPreparing.impl

import android.content.Context
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingPreparingScreenArgs
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.buttons.BackButton
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.decorations.CircleBackgroundDecoration
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.ExhaleScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.TakeABreathScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.WelcomeScreen
import javax.inject.Inject

class TrainingPreparingScreenImpl @Inject constructor() : TrainingPreparingScreen {

    @Composable
    override fun TrainingPreparingScreen(
        usbDeviceInteractor: UsbDeviceInteractor,
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingPreparingScreenArgs: TrainingPreparingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    ) {
        val context = LocalContext.current
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

        val viewModel: TrainingPreparingScreenViewModel = viewModel(
            factory = viewModelFactory {
                TrainingPreparingScreenViewModel(
                    usbDeviceInteractor,
                    bleDeviceInteractor,
                )
            }
        )

        val navigateBack = {
            navigateToRoute(Routes.trainingsList)
            viewModel.disconnect()
        }

        val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.subscribeForSettingsChanges()
        }

        BackHandler {
            navigateBack()
        }

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.disconnect()
            }
        }

        TrainingPreparingScreenContent(
            currentScreen = currentScreen,
            onBackButtonClick = navigateBack,
            onContinueButtonClick = {
                viewModel.startCollectingAndNormalizingSensorData(
                    usbManager = usbManager,
                    onTimerEnded = {
                        val route = Routes.generalTrainingRoute
                            .format(
                                trainingPreparingScreenArgs.trainingRoute,
                                trainingPreparingScreenArgs.bleDeviceHardwareAddress
                            )
                        navigateToRoute(route)
                    }
                )
            }
        )
    }

    @Composable
    private fun TrainingPreparingScreenContent(
        currentScreen: CurrentScreen,
        onBackButtonClick: () -> Unit,
        onContinueButtonClick: () -> Unit,
    ) = Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        CircleBackgroundDecoration(
            modifier = Modifier
                .align(Alignment.TopEnd)
        )

        BackButton(
            onClick = onBackButtonClick,
            modifier = Modifier
                .padding(
                    start = Dimensions.primaryVerticalPadding,
                    top = Dimensions.primaryVerticalPadding,
                )
        )

        when (currentScreen) {
            CurrentScreen.Welcome -> WelcomeScreen(
                onContinueButtonClick = onContinueButtonClick,
            )
            CurrentScreen.TakeABreath -> TakeABreathScreen()
            CurrentScreen.Exhale -> ExhaleScreen()
        }
    }

    private companion object {
        val TAG: String = TrainingPreparingScreenImpl::class.java.simpleName
    }
}
