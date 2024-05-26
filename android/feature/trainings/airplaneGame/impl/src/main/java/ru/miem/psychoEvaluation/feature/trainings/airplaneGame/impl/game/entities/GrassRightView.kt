package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.grassRightView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) = GrassRightView(x, screenHeight, scrollSpeed).addTo(this)

class GrassRightView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
): LeftScrollable(
    x,
    screenHeight - AssetLoader.grassRight.height,
    AssetLoader.grassRight.width,
    scrollSpeed
) {
    private val grassImage = scrollingContainer.image(AssetLoader.grassRight) {
        smoothing = false
    }
}