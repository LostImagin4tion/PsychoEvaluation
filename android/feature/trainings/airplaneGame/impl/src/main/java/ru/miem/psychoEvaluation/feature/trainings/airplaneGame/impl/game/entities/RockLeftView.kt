package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.rockLeftView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) = RockLeftView(x, screenHeight, scrollSpeed).addTo(this)

class RockLeftView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double,
) : LeftScrollable(
    x,
    screenHeight - AssetLoader.rockLeft.height,
    AssetLoader.rockLeft.width,
    scrollSpeed
) {
    private val rockImage = scrollingContainer.image(AssetLoader.rockLeft) {
        smoothing = false
    }
}