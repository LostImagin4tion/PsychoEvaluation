package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.grassMiddleView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) = GrassMiddleView(x, screenHeight, scrollSpeed).addTo(this)

class GrassMiddleView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) : LeftScrollable(
    x,
    screenHeight - AssetLoader.grassMiddle.height,
    AssetLoader.grassMiddle.width,
    scrollSpeed
) {
    private val grassImage = scrollingContainer.image(AssetLoader.grassMiddle) {
        smoothing = false
    }
}
