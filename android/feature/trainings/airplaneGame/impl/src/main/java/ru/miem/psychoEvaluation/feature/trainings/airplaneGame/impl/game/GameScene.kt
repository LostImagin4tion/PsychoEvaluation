package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import android.content.Context
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.addUpdater
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.GameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.gameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

class GameScene(
    private val screenWidth: Double,
    private val screenHeight: Double,
    private val context: Context,
    private val dataFlow: StateFlow<SensorData>,
    private val increaseGameDifficulty: () -> Unit,
    private val decreaseGameDifficulty: () -> Unit,
) : Scene() {

    private var gameWorld: GameWorld? = null
    private var previousData: Double? = null

    override suspend fun SContainer.sceneInit() {
        setSize(screenWidth, screenHeight)
        AssetLoader.load()
    }

    override suspend fun SContainer.sceneMain() {
        var gameTime = 0.seconds

        gameWorld = gameWorld(
            screenWidth,
            screenHeight,
            context,
            onGameOver = {
                when {
                    gameTime < 15.seconds -> decreaseGameDifficulty()
                    gameTime > 60.seconds -> increaseGameDifficulty
                }
                gameTime = 0.seconds
            }
        )

        val input = views.input

        addUpdater { delta ->
            gameTime += delta.seconds.seconds

            if (input.mouseButtons != 0) {
                if (gameWorld?.isReady == true) {
                    gameWorld?.start()
                }
                if (gameWorld?.isGameOver == true) {
                    previousData = null
                    gameWorld?.restart()
                }
            } else if (gameWorld?.isRunning == true) {
                val currentData = dataFlow.value
                val normalizedData = currentData.normalizedData

//                val bordersHeight = with(currentData) { upperLimit - lowerLimit }
//                val data = normalizedData * screenHeight / bordersHeight

                if (previousData == null) {
                    previousData = normalizedData
                }

                val speed = previousData?.let {
                    (normalizedData - it) / delta.seconds
                }

                Timber.tag("HELLO").i("$currentData speed $speed previous $previousData current $normalizedData")

                if (speed != null && speed != 0.0) {
                    previousData = normalizedData
                    gameWorld?.onNewData(normalizedData, speed)
                }
            }

            gameWorld?.update(delta)
        }
    }

    override suspend fun sceneDestroy() {
        gameWorld?.onDestroy()
        gameWorld = null
        super.sceneDestroy()
    }
}
