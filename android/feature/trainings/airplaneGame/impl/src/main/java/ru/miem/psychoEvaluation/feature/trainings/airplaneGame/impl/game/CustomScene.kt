package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.addUpdater
import com.soywiz.korio.async.ObservableProperty
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.gameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader

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
        val world = gameWorld(widthParam, heightParam, highScore)

        val input = views.input

        addUpdater { delta ->
            if (input.mouseButtons != 0) { // TODO this algorithm thinks that a long click is multiple clicks
                if (world.isReady) {
                    world.start()
                }

                world.onClick()

                if (world.isGameOver || world.isHighScore) {
                    world.restart()
                }
            }

            world.update(delta)
        }
    }
}
