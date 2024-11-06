package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import android.content.Context
import android.os.Environment
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.addUpdater
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.designSystem.utils.toString
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.GameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.gameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class GameScene(
    private val screenWidth: Double,
    private val screenHeight: Double,
    private val context: Context,
    private val dataFlow: StateFlow<SensorData>,
    private val maxGameTime: Duration,
    private val onGameEnded: (Duration, Date, Duration, Duration, Duration, Int) -> Unit,
    private val onStartButtonClick: () -> Unit,
    private val onSettingsButtonClick: () -> Unit,
    private val onStatisticsButtonClick: (Duration, Duration, Duration, Duration, Int) -> Unit,
    private val onExitButtonClick: () -> Unit,
    private val increaseGameDifficulty: () -> Unit,
    private val decreaseGameDifficulty: () -> Unit,
) : Scene() {

    private var gameWorld: GameWorld? = null
    private var previousData: Double? = null

    private var fileOutputWriter: BufferedWriter? = null

    override suspend fun SContainer.sceneInit() {
        setSize(screenWidth, screenHeight)
        AssetLoader.load()
    }

    override suspend fun SContainer.sceneMain() {
//        var totalGameTime = 0.seconds
        var currentGameTime = 0.seconds
        var currentGameDate = Calendar.getInstance().time

        gameWorld = gameWorld(
            screenWidth,
            screenHeight,
            onStartButtonClick = {
                onStartButtonClick()
                currentGameTime = 0.seconds
                currentGameDate = Calendar.getInstance().time
                setupFileInputStream(context)
                previousData = null
            },
            onSettingsButtonClick = {
                closeStream()
                onSettingsButtonClick()
            },
            onStatisticsButtonClick = {
                    timeInCorridor,
                    timeUpperCorridor,
                    timeLowerCorridor,
                    numberOfFlightsOutsideCorridor,
                ->
                closeStream()
                onStatisticsButtonClick(
                    currentGameTime,
                    timeInCorridor,
                    timeUpperCorridor,
                    timeLowerCorridor,
                    numberOfFlightsOutsideCorridor
                )
            },
            onExitButtonClick = {
                closeStream()
                onExitButtonClick()
            },
            onGameOver = {
                    timeInCorridor,
                    timeUpperCorridor,
                    timeLowerCorridor,
                    numberOfFlightsOutsideCorridor,
                ->
//                when {
//                    totalGameTime < 15.seconds -> decreaseGameDifficulty()
//                    totalGameTime > 60.seconds -> increaseGameDifficulty
//                }
//                totalGameTime = 0.seconds
                onGameEnded(
                    currentGameTime,
                    currentGameDate,
                    timeInCorridor,
                    timeUpperCorridor,
                    timeLowerCorridor,
                    numberOfFlightsOutsideCorridor
                )
            }
        )

        addUpdater { delta ->
//            totalGameTime += delta.seconds.seconds

            if (gameWorld?.isRunning == true) {
                currentGameTime += delta.seconds.seconds

                val currentData = dataFlow.value
                val normalizedData = currentData.normalizedData

                if (previousData == null) {
                    previousData = normalizedData
                }

                val speed = previousData?.let {
                    (normalizedData - it) / delta.seconds
                }

//                Timber.tag("HELLO").d("$currentData speed $speed previous $previousData current $normalizedData")

                if (speed != null && speed != 0.0) {
                    previousData = normalizedData
                    gameWorld?.onNewData(speed, currentGameTime)
                }
                fileOutputWriter?.write("${currentData.rawData}\n")

                if (currentGameTime > maxGameTime) {
                    gameWorld?.finishGame()
                }
            } else if (gameWorld?.isGameOver == true) {
                closeStream()
            }

            gameWorld?.update(delta)
        }
    }

    override suspend fun sceneDestroy() {
        gameWorld?.onDestroy()
        gameWorld = null
        closeStream()
        super.sceneDestroy()
    }

    private fun setupFileInputStream(context: Context) {
        try {
            closeStream()

            val datetime = Calendar.getInstance().time.toString("yyyy-MM-dd_HH:mm:ss")
            val filename = "airplane-psycho-$datetime.txt"

            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            fileOutputWriter = file.bufferedWriter()

            Timber.tag(TAG).i("Created new file ${file.absolutePath}")
        } catch (e: IOException) {
            Timber.tag(TAG).e("Got IO error while writing data to file: $e ${e.message}")
        }
    }

    private fun closeStream() {
        fileOutputWriter?.let {
            it.flush()
            it.close()
            Timber.tag(TAG).i("Closed file output writer")
        }
        fileOutputWriter = null
    }

    private companion object {
        private val TAG = GameScene::class.java.simpleName
    }
}
