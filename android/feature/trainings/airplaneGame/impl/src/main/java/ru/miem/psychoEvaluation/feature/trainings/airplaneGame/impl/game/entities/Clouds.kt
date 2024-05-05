package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.clouds(
    x: Double,
    y: Double,
    scrollSpeed: Double,
) = Clouds(
    x = x,
    y = y,
    scrollSpeed = scrollSpeed,
).addTo(this)

class Clouds(
    x: Double,
    y: Double,
    scrollSpeed: Double,
) :
    LeftScrollable(x, y, AssetLoader.clouds.width, scrollSpeed) {

    private val cloudsImage = scrollingContainer.image(AssetLoader.clouds) { smoothing = false }
}
