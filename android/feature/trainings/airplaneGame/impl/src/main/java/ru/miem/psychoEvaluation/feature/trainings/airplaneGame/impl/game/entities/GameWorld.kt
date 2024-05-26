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
    context: Context
) = GameWorld(screenWidth, screenHeight, context).addTo(this)

private enum class GameState {
    READY,
    RUNNING,
    GAME_OVER,
}

class GameWorld(
    override var width: Double,
    override var height: Double,
    private val context: Context
) : Container() {

    private val midPointX = width / 2.0
    private val midPointY = height / 2.0

    private var score = 0

    val isReady get() = currentState == GameState.READY
    val isRunning get() = currentState == GameState.RUNNING
    val isGameOver get() = currentState == GameState.GAME_OVER

    private var currentState = GameState.READY

    private val backgroundView = backgroundView(width, height)
    private val airplane = airplaneView(x = 700.0, y = midPointY)
    private val scroller = scrollHandler(width, height)

    private val welcomeText = welcomeText(x = midPointX, y = 300.0)
    private val gameOverText = gameOverText(x = midPointX, y = 300.0)

    fun start() {
        currentState = GameState.RUNNING
        airplane.onStart(context)
    }

    fun restart() {
        currentState = GameState.READY
        score = 0
        scroller.onRestart()
        airplane.onRestart(midPointY, context)
    }

    fun update(delta: TimeSpan) {
        when (currentState) {
            GameState.READY -> {
                welcomeText.visible = true
                gameOverText.visible = false
            }
            GameState.RUNNING -> {
                gameOverText.visible = false
                updateRunning(delta)
            }
            GameState.GAME_OVER -> {
                gameOverText.visible = true
                updateRunning(delta)
            }
        }
    }

    fun onNewData(rawData: Int, speed: Double) = airplane.onNewData(rawData, speed)

    fun onDestroy() {
        airplane.onDestroy()
    }

    private fun updateRunning(delta: TimeSpan) {
        welcomeText.visible = false

        airplane.update(delta)
        scroller.update(delta)

        if (currentState == GameState.RUNNING &&
            (airplane.highestY <= 0 || airplane.lowestY > height)
        ) {
            scroller.stop()
            airplane.die()

            currentState = GameState.GAME_OVER
        }
    }
}
