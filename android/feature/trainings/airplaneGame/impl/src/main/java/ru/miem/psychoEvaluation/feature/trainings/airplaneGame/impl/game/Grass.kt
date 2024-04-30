package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.helpers.AssetLoader

fun Container.grass(x: Double, y: Double, scrollSpeed: Double) = Grass(x, y, scrollSpeed).addTo(this)

class Grass(x: Double, y: Double, scrollSpeed: Double) : LeftScrollable(x, y, AssetLoader.grass.width, scrollSpeed) {

    init {
        scrollingContainer.image(AssetLoader.grass) { smoothing = false }
    }
}
