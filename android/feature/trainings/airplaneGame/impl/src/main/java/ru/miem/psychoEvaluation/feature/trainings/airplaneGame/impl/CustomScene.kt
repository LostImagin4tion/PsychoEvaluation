package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl

import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.text
import com.soywiz.korim.bitmap.extract
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.async.launchImmediately
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.gameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.helpers.AssetLoader
import kotlin.math.roundToInt

class CustomScene(
    private val widthParam: Double,
    private val heightParam: Double,
) : Scene() {

    private val highScore = ObservableProperty(0)

    override suspend fun SContainer.sceneInit() {
        setSize(widthParam, heightParam)
        AssetLoader.load()
    }

    override suspend fun SContainer.sceneMain() {
        views.gameWindow.icon = AssetLoader.birdMid.extract()

        val world = gameWorld(widthParam, heightParam, highScore)

        val fpsText = text("FPS: ...").alignBottomToBottomOf(this)

        val input = views.input

        addUpdater { delta ->
            launchImmediately {
                if (input.mouseButtons != 0) {  // TODO this algorithm thinks that a long click is multiple clicks
                    if (world.isReady) {
                        world.start()
                    }

                    world.onClick()

                    if (world.isGameOver || world.isHighScore) {
                        world.restart()
                    }
                }

                fpsText.text = "FPS: ${(1 / delta.seconds).roundToInt()}"

                world.update(delta)
            }
        }
    }
}
