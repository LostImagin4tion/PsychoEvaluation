package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import android.content.Context
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameOverText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.welcomeText

fun Container.gameWorld(
    screenWidth: Double,
    screenHeight: Double,
    context: Context,
    onGameOver: () -> Unit,
) = GameWorld(
    screenWidth,
    screenHeight,
    context,
    onGameOver,
).addTo(this)

private enum class GameState {
    Ready,
    Running,
    GameOver,
}

class GameWorld(
    override var width: Double,
    override var height: Double,
    private val context: Context,
    private val onGameOver: () -> Unit,
) : Container() {

    private val midPointX = width / 2.0
    private val midPointY = height / 2.0

    private var score = 0

    private var currentState = GameState.Ready

    private val backgroundView = backgroundView(width, height)
    private val airplane = airplaneView(x = 700.0, y = midPointY)
    private val scroller = scrollHandler(width, height)

    private val welcomeText = welcomeText(x = midPointX, y = 300.0)
    private val gameOverText = gameOverText(x = midPointX, y = 300.0)

    val isReady get() = currentState == GameState.Ready
    val isRunning get() = currentState == GameState.Running
    val isGameOver get() = currentState == GameState.GameOver

    fun start() {
        currentState = GameState.Running
        airplane.onStart(context)
    }

    fun restart() {
        currentState = GameState.Ready
        score = 0
        scroller.onRestart()
        airplane.onRestart(midPointY, context)
    }

    fun update(delta: TimeSpan) {
        when (currentState) {
            GameState.Ready -> {
                welcomeText.visible = true
                gameOverText.visible = false
            }
            GameState.Running -> {
                gameOverText.visible = false
                updateRunning(delta)
            }
            GameState.GameOver -> {
                gameOverText.visible = true
                updateRunning(delta)
            }
        }
    }

    fun onNewData(rawData: Double, speed: Double) = airplane.onNewData(rawData, speed)

    fun onDestroy() {
        airplane.onDestroy()
    }

    private fun updateRunning(delta: TimeSpan) {
        welcomeText.visible = false

        airplane.update(delta)
        scroller.update(delta)

        if (currentState == GameState.Running &&
            (airplane.highestY <= 0 || airplane.lowestY > height)
        ) {
            scroller.stop()
            airplane.die()

            currentState = GameState.GameOver
            onGameOver()
        }
    }
}
