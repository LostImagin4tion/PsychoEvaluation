package ru.miem.psychoEvaluation.feature.trainingPreparing.impl

import android.content.Context
import android.hardware.usb.UsbManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.buttons.BackButton
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingPreparingScreenArgs
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.TrainingPreparingScreenState
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.decorations.CircleBackgroundDecoration
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.ExhaleScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.HoldYourBreathScreen
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

        var isTimerEnded by rememberSaveable { mutableStateOf(false) }

        val navigateBack = {
            navigateToRoute(Routes.trainingsList)
            viewModel.disconnect()
        }

        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        LaunchedEffect(isTimerEnded) {
            if (isTimerEnded) {
                val route = Routes.generalTrainingRoute
                    .format(
                        trainingPreparingScreenArgs.trainingRoute,
                        trainingPreparingScreenArgs.bleDeviceHardwareAddress
                    )
                navigateToRoute(route)
            }
        }

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
            screenState = screenState,
            onBackButtonClick = navigateBack,
            onContinueButtonClick = {
                viewModel.startCollectingAndNormalizingSensorData(
                    usbManager = usbManager,
                    onTimerEnded = { isTimerEnded = true }
                )
            }
        )
    }

    @Composable
    private fun TrainingPreparingScreenContent(
        screenState: TrainingPreparingScreenState,
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

        when (screenState.currentScreen) {
            CurrentScreen.Welcome -> WelcomeScreen(
                onContinueButtonClick = onContinueButtonClick,
            )
            CurrentScreen.TakeABreath -> TakeABreathScreen(
                roundNumber = screenState.roundNumberString,
                progress = screenState.screenProgress,
            )
            CurrentScreen.HoldYourBreath -> HoldYourBreathScreen(
                roundNumber = screenState.roundNumberString,
                progress = screenState.screenProgress,
            )
            CurrentScreen.Exhale -> ExhaleScreen(
                roundNumber = screenState.roundNumberString,
                progress = screenState.screenProgress,
            )
        }
    }

    private companion object {
        val TAG: String = TrainingPreparingScreenImpl::class.java.simpleName
    }
}
