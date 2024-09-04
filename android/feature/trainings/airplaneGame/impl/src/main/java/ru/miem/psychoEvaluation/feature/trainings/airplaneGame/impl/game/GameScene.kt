package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import android.content.Context
import android.os.Environment
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.addUpdater
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.GameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.gameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.model.SensorData
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

    private val calendar = Calendar.getInstance()
    private var fileOutputWriter: BufferedWriter? = null

    override suspend fun SContainer.sceneInit() {
        setSize(screenWidth, screenHeight)
        AssetLoader.load()
    }

    override suspend fun SContainer.sceneMain() {
        var gameTime = 0.seconds

        gameWorld = gameWorld(
            screenWidth,
            screenHeight,
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
                    setupFileInputStream(context)
                }
                if (gameWorld?.isGameOver == true) {
                    previousData = null
                    gameWorld?.restart()
                    setupFileInputStream(context)
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

                Timber.tag("HELLO").d("$currentData speed $speed previous $previousData current $normalizedData")

                if (speed != null && speed != 0.0) {
                    previousData = normalizedData
                    gameWorld?.onNewData(speed)
                    fileOutputWriter?.write("${currentData.rawData}\n")
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
        super.sceneDestroy()
    }

    private fun setupFileInputStream(context: Context) {
        try {
            closeStream()

            val datetime = calendar.time.toString("yyyy-MM-dd_HH:mm:ss")
            val filename = "$datetime-psycho.txt"

            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            fileOutputWriter = file.bufferedWriter()

            Timber.tag(TAG).i(
                "Created new file ${file.absolutePath}, " +
                    "all files: ${file.parentFile?.listFiles()?.map { it.name }}"
            )
        } catch (e: IOException) {
            Timber.tag(TAG).e("Got IO error while writing data to file: $e ${e.message}")
        }
    }

    private fun closeStream() {
        fileOutputWriter?.let {
            it.close()
            Timber.tag(TAG).i("Closed file output writer")
        }
        fileOutputWriter = null
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private companion object {
        private val TAG = GameScene::class.java.simpleName
    }
}
