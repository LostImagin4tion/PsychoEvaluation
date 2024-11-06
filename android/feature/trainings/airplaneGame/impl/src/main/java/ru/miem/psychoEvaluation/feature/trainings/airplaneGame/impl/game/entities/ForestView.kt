package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.forestView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) = ForestView(x, screenHeight, scrollSpeed).addTo(this)

class ForestView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) : LeftScrollable(
    x,
    screenHeight - AssetLoader.forestLeft.height,
    AssetLoader.forestLeft.width,
    scrollSpeed
) {
    private val forestImage = scrollingContainer.image(AssetLoader.forestLeft) {
        smoothing = false
    }
}