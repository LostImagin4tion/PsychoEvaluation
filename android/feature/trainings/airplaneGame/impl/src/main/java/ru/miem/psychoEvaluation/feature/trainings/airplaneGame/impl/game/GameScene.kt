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

class GameScene(
    private val screenWidth: Double,
    private val screenHeight: Double,
    private val context: Context,
    private val dataFlow: StateFlow<SensorData>
) : Scene() {

    private var gameWorld: GameWorld? = null
    private var previousData: Double? = null

    override suspend fun SContainer.sceneInit() {
        setSize(screenWidth, screenHeight)
        AssetLoader.load()
    }

    override suspend fun SContainer.sceneMain() {
        gameWorld = gameWorld(screenWidth, screenHeight, context)

        val input = views.input

        addUpdater { delta ->
            if (input.mouseButtons != 0) { // TODO this algorithm thinks that a long click is multiple clicks
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
                if (previousData == null) {
                    previousData = normalizedData
                }

                val speed = previousData?.let {
                    (normalizedData - it) / delta.seconds
                }

                if (speed != null && speed != 0.0) {
                    Timber.tag("HELLO").i("speed $speed previous $previousData current $currentData")
                    previousData = currentData.normalizedData
                    gameWorld?.onNewData(currentData.rawData, speed)
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
