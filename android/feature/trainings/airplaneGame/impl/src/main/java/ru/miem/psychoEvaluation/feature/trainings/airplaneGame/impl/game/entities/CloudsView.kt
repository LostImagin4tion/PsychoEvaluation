package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils.LeftScrollable

fun Container.cloudsView(
    x: Double,
    y: Double,
    scrollSpeed: Double,
) = CloudsView(x, y, scrollSpeed).addTo(this)

class CloudsView(
    x: Double,
    y: Double,
    scrollSpeed: Double,
) : LeftScrollable(
    x,
    y,
    AssetLoader.clouds.width,
    scrollSpeed
) {

    private val cloudsImage = scrollingContainer.image(AssetLoader.clouds) {
        smoothing = false
    }
}
