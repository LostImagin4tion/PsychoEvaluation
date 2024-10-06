package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.xy
import ru.miem.psychoEvaluation.common.designSystem.utils.dpd
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameOverText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameTimeText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.welcomeText
import kotlin.time.Duration

fun Container.gameWorld(
    screenWidth: Double,
    screenHeight: Double,
    onStartButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onExitButtonClick: () -> Unit,
    onGameOver: () -> Unit,
) = GameWorld(
    screenWidth,
    screenHeight,
    onStartButtonClick,
    onSettingsButtonClick,
    onExitButtonClick,
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
    private val onStartButtonClick: () -> Unit,
    private val onSettingsButtonClick: () -> Unit,
    private val onExitButtonClick: () -> Unit,
    private val onGameOver: () -> Unit,
) : Container() {

    private val midPointX = width / 2.0
    private val midPointY = height / 2.0

    private var score = 0

    private var currentState = GameState.Ready

    private val backgroundView = backgroundView(width, height)
    private val airplane = airplaneView(
        x = 150.dpd,
        y = midPointY,
        lowestY = 0.0,
        highestY = height,
    )
    private val scroller = scrollHandler(width, height)

    private val welcomeText = welcomeText(x = midPointX, y = 80.dpd)
    private val gameTimeText = gameTimeText(x = width - 50.dpd, y = 16.dpd)
    private val gameOverText = gameOverText(x = midPointX, y = 80.dpd)

    private val upperGsrBorder = gsrBorderView(width = width, height = 1.dpd, y = height * 0.1)
    private val lowerGsrBorder = gsrBorderView(width = width, height = 1.dpd, y = height * 0.9)

    private val startButton = imageButton(
        AssetLoader.startButton,
        width = 96.dpd,
        height = 34.dpd,
        onClick = {
            onStartButtonClick()
            when {
                isReady -> start()
                isGameOver -> restart()
            }
        }
    ).apply {
        xy(midPointX - 75.dpd, midPointY + 85.dpd)
    }

    private val settingsButton = imageButton(
        AssetLoader.settingsButton,
        width = 96.dpd,
        height = 34.dpd,
        onClick = { onSettingsButtonClick() }
    ).apply {
        xy(midPointX + 75.dpd, midPointY + 85.dpd)
    }

//    private val exitButton = imageButton(
//        AssetLoader.exitButton,
//        width = 336.0,
//        height = 120.0,
//        onClick = { onExitButtonClick() }
//    ).apply {
//        xy(this@GameWorld.width - 200, 100.0)
//    }

    val isReady get() = currentState == GameState.Ready
    val isRunning get() = currentState == GameState.Running
    val isGameOver get() = currentState == GameState.GameOver

    fun start() {
        currentState = GameState.Running
        airplane.onStart()
    }

    fun restart() {
        score = 0
        scroller.onRestart()
        airplane.onRestart(midPointY)
        start()
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
                startButton.visible = true
                settingsButton.visible = true
            }
            GameState.Running -> {
                welcomeText.visible = false
                gameOverText.visible = false
                gameTimeText.visible = true
                startButton.visible = false
                settingsButton.visible = false
                updateRunning(delta)
            }
            GameState.GameOver -> {
                welcomeText.visible = false
                gameOverText.visible = true
                gameTimeText.visible = true
                startButton.visible = true
                settingsButton.visible = true
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

//        if (currentState == GameState.Running &&
//            (airplane.highestY <= 0 || airplane.lowestY > height)
//        ) {
//            finishGame()
//        }
    }
}
