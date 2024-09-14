package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameOverText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameTimeText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.welcomeText
import kotlin.time.Duration

fun Container.gameWorld(
    screenWidth: Double,
    screenHeight: Double,
    onGameOver: () -> Unit,
) = GameWorld(
    screenWidth,
    screenHeight,
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
    private val gameTimeText = gameTimeText(x = width - 200.0, y = 100.0)
    private val gameOverText = gameOverText(x = midPointX, y = 300.0)

    val isReady get() = currentState == GameState.Ready
    val isRunning get() = currentState == GameState.Running
    val isGameOver get() = currentState == GameState.GameOver

    fun start() {
        currentState = GameState.Running
        airplane.onStart()
    }

    fun restart() {
        currentState = GameState.Ready
        score = 0
        scroller.onRestart()
        airplane.onRestart(midPointY)
    }

    fun finishGame() {
        scroller.stop()
        airplane.die()
        currentState = GameState.GameOver
        onGameOver()
    }

    fun update(delta: TimeSpan) {
        when (currentState) {
            GameState.Ready -> {
                welcomeText.visible = true
                gameTimeText.visible = false
                gameOverText.visible = false
            }
            GameState.Running -> {
                welcomeText.visible = false
                gameOverText.visible = false
                gameTimeText.visible = true
                updateRunning(delta)
            }
            GameState.GameOver -> {
                welcomeText.visible = false
                gameOverText.visible = true
                gameTimeText.visible = true
                updateRunning(delta)
            }
        }
    }

    fun onNewData(speed: Double, gameTime: Duration) {
        gameTimeText.changeTime(gameTime)
        airplane.onNewData(speed)
    }

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
            finishGame()
        }
    }
}
