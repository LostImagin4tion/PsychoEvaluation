package ru.miem.psychoEvaluation.feature.trainingPreparing.impl

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.trainingPreparing.api.TrainingPreparingScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.state.CurrentScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.ExhaleScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.TakeABreathScreen
import ru.miem.psychoEvaluation.feature.trainingPreparing.impl.ui.screens.WelcomeScreen
import timber.log.Timber
import javax.inject.Inject

class TrainingPreparingScreenImpl @Inject constructor() : TrainingPreparingScreen {

    @Composable
    override fun TrainingPreparingScreen(
        navigateToRoute: (route: String) -> Unit,
        showMessage: (String) -> Unit,
        navigateToTraining: () -> Unit,
    ) {
        val viewModel: TrainingPreparingScreenViewModel = viewModel()

        val currentScreen = viewModel.currentScreen.collectAsState()

        BackHandler {
            navigateToRoute(Routes.trainingsList)
        }

        when (currentScreen.value) {
            CurrentScreen.Welcome -> WelcomeScreen {
                viewModel.startCollectingAndNormalizingSensorData {
                    Timber.tag(TAG).d("HELLO TIMER ENDED")
                    navigateToTraining()
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