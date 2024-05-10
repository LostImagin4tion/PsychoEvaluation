package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korio.async.ObservableProperty
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.backgroundView
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.gameOverText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.scoreText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.welcomeText

fun Container.gameWorld(
    width: Double,
    height: Double,
    gameScoreProperty: ObservableProperty<Int>
) = GameWorld(width, height, gameScoreProperty).addTo(this)

private enum class GameState {
    READY,
    RUNNING,
    GAME_OVER,
    HIGH_SCORE,
}

class GameWorld(
    override var width: Double,
    override var height: Double,
    private val gameScoreProperty: ObservableProperty<Int>
) : Container() {

    private val midPointX = width / 2.0
    private val midPointY = height / 2.0

    private var score = 0

    val isReady get() = currentState == GameState.READY
    val isRunning get() = currentState == GameState.RUNNING
    val isGameOver get() = currentState == GameState.GAME_OVER
    val isHighScore get() = currentState == GameState.HIGH_SCORE

    private var currentState = GameState.READY

    private val backgroundView = backgroundView(width, height)
    private val airplane = airplane(x = 300.0, y = midPointY)
    private val scroller = scrollHandler()

    private val scoreText = scoreText()
    private val welcomeText = welcomeText(x = midPointX, y = midPointY)
    private val gameOverText = gameOverText(x = midPointX, y = midPointY)

    init {
        updateScoreViews()
    }

    fun start() {
        currentState = GameState.RUNNING
    }

    fun restart() {
        val midPointY = height / 2.0
        currentState = GameState.READY
        score = 0
        scroller.onRestart()
        airplane.onRestart(midPointY)
        updateScoreViews()
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
            GameState.HIGH_SCORE -> {
                gameOverText.visible = false

                updateRunning(delta)
            }
        }
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
            airplane.decelerate()

            currentState = if (score > gameScoreProperty.value) {
                gameScoreProperty.update(score)
                GameState.HIGH_SCORE
            } else {
                GameState.GAME_OVER
            }
        }
    }

    fun onNewData(speed: Double) = airplane.onNewData(speed)

    private fun updateScoreViews() {
        scoreText.updateScoreView(score = score, x = midPointX, y = 128.0)
    }
}
