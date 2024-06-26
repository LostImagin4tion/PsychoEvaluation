package ru.miem.psychoEvaluation.feature.trainingsList.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.miem.psychoEvaluation.common.designSystem.text.TitleText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.trainingsList.api.TrainingsListScreen
import javax.inject.Inject

class TrainingsListScreenImpl @Inject constructor() : TrainingsListScreen {

    @Composable
    override fun TrainingsListScreen(
        navController: NavHostController,
        showMessage: (String) -> Unit
    ) {
        TrainingsScreenContent(
            navigateToDebugTraining = { navController.navigate(Routes.debugTraining) },
            navigateToAirplaneGame = { navController.navigate(Routes.airplaneGame) },
        )
    }

    @Composable
    private fun TrainingsScreenContent(
        navigateToDebugTraining: () -> Unit,
        navigateToAirplaneGame: () -> Unit,
    ) = Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding()
    ) {
        // ===== UI SECTION =====

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        TitleText(textRes = R.string.trainings_header, isLarge = false)

        Spacer(modifier = Modifier.height(Dimensions.mainVerticalPadding * 2))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { // === Debug Training ===
                TrainingCard(
                    titleRes = R.string.debug_training_title,
                    descriptionRes = R.string.debug_training_description,
                    imageRes = R.drawable.debug_training_icon,
                    modifier = Modifier.padding(bottom = Dimensions.mainVerticalPadding),
                    onClick = navigateToDebugTraining
                )
            }

            item { // === Concentration Training ===
                TrainingCard(
                    titleRes = R.string.concentration_training_title,
                    descriptionRes = R.string.concentration_training_description,
                    imageRes = R.drawable.concentration_training_icon,
                    onClick = navigateToAirplaneGame
                )
            }
        }
    }
}
