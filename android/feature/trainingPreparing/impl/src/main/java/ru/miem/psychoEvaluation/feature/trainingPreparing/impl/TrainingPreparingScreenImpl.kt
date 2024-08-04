package ru.miem.psychoEvaluation.feature.trainingPreparing.impl

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.common.designSystem.utils.viewModelFactory
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.screenArgs.TrainingPreparingScreenArgs
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.ExhaleScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.TakeABreathScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.WelcomeScreen
import javax.inject.Inject

class TrainingPreparingScreenImpl @Inject constructor() : TrainingPreparingScreen {

    @Composable
    override fun TrainingPreparingScreen(
        bleDeviceInteractor: BluetoothDeviceInteractor,
        trainingPreparingScreenArgs: TrainingPreparingScreenArgs,
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
    ) {
        val viewModel: TrainingPreparingScreenViewModel = viewModel(
            factory = viewModelFactory { TrainingPreparingScreenViewModel(bleDeviceInteractor) }
        )

        val currentScreen = viewModel.currentScreen.collectAsState()

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        when (currentScreen.value) {
            CurrentScreen.Welcome -> WelcomeScreen {
                viewModel.startCollectingAndNormalizingSensorData {
                    val route = Routes.generalTrainingRoute
                        .format(
                            trainingPreparingScreenArgs.trainingRoute,
                            trainingPreparingScreenArgs.bleDeviceHardwareAddress
                        )
                    navigateToRoute(route)
                }
            }
            CurrentScreen.TakeABreath -> TakeABreathScreen()
            CurrentScreen.Exhale -> ExhaleScreen()
        }
    }

    private companion object {
        val TAG: String = TrainingPreparingScreenImpl::class.java.simpleName
    }
}
