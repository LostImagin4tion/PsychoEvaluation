package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.RGBA
import ru.miem.psychoEvaluation.common.designSystem.utils.dpd
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameOverText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.gameTimeText
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text.welcomeText
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun Container.gameWorld(
    screenWidth: Double,
    screenHeight: Double,
    onStartButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onStatisticsButtonClick: (Duration, Duration, Duration, Int) -> Unit,
    onExitButtonClick: () -> Unit,
    onGameOver: (Duration, Duration, Duration, Int) -> Unit,
) = GameWorld(
    screenWidth,
    screenHeight,
    onStartButtonClick,
    onSettingsButtonClick,
    onStatisticsButtonClick,
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
    private val onStatisticsButtonClick: (Duration, Duration, Duration, Int) -> Unit,
    private val onExitButtonClick: () -> Unit,
    private val onGameOver: (Duration, Duration, Duration, Int) -> Unit,
) : Container() {

    private val midPointX = width / 2.0
    private val midPointY = height / 2.0

    private var score = 0
    private var timeInCorridor = 0.seconds
    private var timeUpperCorridor = 0.seconds
    private var timeLowerCorridor = 0.seconds
    private var numberOfFlightsFromOutsideCorridor = 0
    private var airplaneWasInsideCorridor = true

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

    private val upperGsrBorder = gsrBorderView(
        width = width,
        height = 1.dpd,
        color = RGBA(0, 0, 0, 255),
    ).apply {
        xy(0.0, this@GameWorld.height * 0.1)
    }

    private val lowerGsrBorder = gsrBorderView(
        width = width,
        height = 1.dpd,
        color = RGBA(255, 255, 255, 255),
    ).apply {
        xy(0.0, this@GameWorld.height * 0.9)
    }

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
        xy(midPointX, midPointY)
    }

    private val statisticsButton = imageButton(
        AssetLoader.statisticsButton,
        width = 96.dpd,
        height = 34.dpd,
        onClick = {
            onStatisticsButtonClick(
                timeInCorridor,
                timeUpperCorridor,
                timeLowerCorridor,
                numberOfFlightsFromOutsideCorridor
            )
        }
    ).apply {
        xy(midPointX, midPointY + 50.dpd)
    }

    private val settingsButton = imageButton(
        AssetLoader.settingsButton,
        width = 96.dpd,
        height = 34.dpd,
        onClick = { onSettingsButtonClick() }
    ).apply {
        xy(midPointX, midPointY + 100.dpd)
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

    fun finishGame() {
        scroller.stop()
        airplane.die()
        currentState = GameState.GameOver
        onGameOver(
            timeInCorridor,
            timeUpperCorridor,
            timeLowerCorridor,
            numberOfFlightsFromOutsideCorridor
        )
    }

    fun update(delta: TimeSpan) {
        when (currentState) {
            GameState.Ready -> {
                welcomeText.visible = true
                gameTimeText.visible = false
                gameOverText.visible = false
                startButton.visible = true
                statisticsButton.visible = false
                settingsButton.visible = true
            }
            GameState.Running -> {
                welcomeText.visible = false
                gameOverText.visible = false
                gameTimeText.visible = true
                startButton.visible = false
                statisticsButton.visible = false
                settingsButton.visible = false
                updateRunning(delta)
                updateStatistics(delta)
            }
            GameState.GameOver -> {
                welcomeText.visible = false
                gameOverText.visible = true
                gameTimeText.visible = true
                startButton.visible = true
                statisticsButton.visible = true
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

    private fun start() {
        currentState = GameState.Running
        timeInCorridor = 0.seconds
        timeUpperCorridor = 0.seconds
        timeLowerCorridor = 0.seconds
        numberOfFlightsFromOutsideCorridor = 0
        airplaneWasInsideCorridor = true
        airplane.onStart()
    }

    private fun restart() {
        score = 0
        scroller.onRestart()
        airplane.onRestart(midPointY)
        start()
    }

    private fun updateStatistics(delta: TimeSpan) {
        val kotlinDelta = delta.seconds.seconds

        if (airplane.highestY >= upperGsrBorder.y && airplane.lowestY <= lowerGsrBorder.y) {
            timeInCorridor += kotlinDelta
            airplaneWasInsideCorridor = true
        } else {
            if (airplaneWasInsideCorridor) {
                airplaneWasInsideCorridor = false
                numberOfFlightsFromOutsideCorridor++
            }

            if (airplane.highestY < upperGsrBorder.y) {
                timeUpperCorridor += kotlinDelta
            } else {
                timeLowerCorridor += kotlinDelta
            }
        }
    }

    private fun updateRunning(delta: TimeSpan) {
        airplane.update(delta)
        scroller.update(delta)

//        if (currentState == GameState.Running &&
//            (airplane.highestY <= 0 || airplane.lowestY > height)
//        ) {
//            finishGame()
//        }
    }
}
