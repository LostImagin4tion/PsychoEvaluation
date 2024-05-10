package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.addUpdater
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.async.launchImmediately
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.models.UsbDeviceData
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities.gameWorld
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import timber.log.Timber

class CustomScene(
    private val widthParam: Double,
    private val heightParam: Double,
    private val dataFlow: StateFlow<UsbDeviceData>
) : Scene() {

    private val highScore = ObservableProperty(0)

    private var previousValue: Double = 0.0
    private var currentValue: Double = 0.0

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
                if (world.isGameOver || world.isHighScore) {
                    world.restart()
                }
            }
            else if (world.isRunning) {
                val newValue = dataFlow.value.normalizedData
                val speed = (newValue - currentValue) / delta.seconds

                previousValue = currentValue
                currentValue = newValue

                Timber.tag("HELLO").i("speed $speed previous $previousValue current $currentValue")

                world.onNewData(speed)
            }

            world.update(delta)
        }
    }
}
