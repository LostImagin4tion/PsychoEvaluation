package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.mountainView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) = MountainView(x, screenHeight, scrollSpeed).addTo(this)

class MountainView(
    x: Double,
    screenHeight: Double,
    scrollSpeed: Double
) : LeftScrollable(
    x,
    screenHeight - AssetLoader.mountain.height,
    AssetLoader.mountain.width,
    scrollSpeed
) {
    private val mountainImage = scrollingContainer.image(AssetLoader.mountain) {
        smoothing = false
    }
}
