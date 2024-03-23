package ru.miem.psychoEvaluation.feature.trainings.impl

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
import ru.miem.psychoEvaluation.designSystem.text.TitleText
import ru.miem.psychoEvaluation.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.trainings.api.TrainingsScreen
import javax.inject.Inject

class TrainingsScreenImpl @Inject constructor() : TrainingsScreen {

    @Composable
    override fun TrainingsScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        TrainingsScreenContent()
    }

    @Composable
    private fun TrainingsScreenContent(

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
            item {
                TrainingCard(
                    titleRes = R.string.concentration_training_title,
                    descriptionRes = R.string.concentration_training_description,
                    imageRes = R.drawable.concentration_training_icon
                )
            }
        }
    }
}