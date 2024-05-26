package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.rockRightView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) = RockRightView(x, screenHeight, scrollSpeed).addTo(this)

class RockRightView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double,
) : LeftScrollable(
    x,
    screenHeight - AssetLoader.rockRight.height,
    AssetLoader.rockRight.width,
    scrollSpeed
) {
    private val rockImage = scrollingContainer.image(AssetLoader.rockRight) {
        smoothing = false
    }
}
