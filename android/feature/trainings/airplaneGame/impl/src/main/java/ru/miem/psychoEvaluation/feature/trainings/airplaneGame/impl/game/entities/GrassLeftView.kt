package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.grassLeftView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double,
) = GrassLeftView(x, screenHeight, scrollSpeed).addTo(this)

class GrassLeftView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) : LeftScrollable(
    x,
    screenHeight - AssetLoader.grassLeft.height,
    AssetLoader.grassLeft.width,
    scrollSpeed
) {
    private val grassImage = scrollingContainer.image(AssetLoader.grassLeft) {
        smoothing = false
    }
}
