package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.soywiz.korge.android.KorgeAndroidView
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import timber.log.Timber
import javax.inject.Inject

class AirplaneGameScreenImpl @Inject constructor() : AirplaneGameScreen {

    @Composable
    override fun AirplaneGameScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        AirplaneGameScreenContent()
    }

    @Composable
    private fun AirplaneGameScreenContent(

    ) {
        AndroidView(
            factory = { context ->
                val displayMetrics = context.resources.displayMetrics
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels
                Timber.tag("HELLO").d("HELLO height $height width  $width")
                KorgeAndroidView(context).apply {
                    loadModule(CustomModule(width, height))
                }
            }
        )
    }
}