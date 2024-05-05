package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.soywiz.korge.android.KorgeAndroidView
import ru.miem.psychoEvaluation.common.designSystem.system.ForceDeviceOrientation
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.api.AirplaneGameScreen
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.CustomModule
import timber.log.Timber
import javax.inject.Inject

class AirplaneGameScreenImpl @Inject constructor() : AirplaneGameScreen {

    @Composable
    override fun AirplaneGameScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        ForceDeviceOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        AirplaneGameScreenContent()
    }

    @Composable
    private fun AirplaneGameScreenContent() {
        AndroidView(
            factory = { context ->
                val displayMetrics = context.resources.displayMetrics
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels
                Timber.tag(TAG).d("Setting height $height and width  $width")
                KorgeAndroidView(context).apply {
                    this.config
                    loadModule(CustomModule(width, height))
                }
            }
        )
    }

    private companion object {
        val TAG: String = AirplaneGameScreenImpl::class.java.simpleName
    }
}
