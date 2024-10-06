package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import com.soywiz.korge.view.size
import com.soywiz.korge.view.solidRect
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.RGBA
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader

fun Container.backgroundView(
    width: Double,
    height: Double,
) = BackgroundView(width, height).addTo(this)

class BackgroundView(
    width: Double,
    height: Double,
) : Container() {

    init {
        // solid color for background
        solidRect(
            width = width,
            height = height,
            color = RGBA(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE, BACKGROUND_ALPHA)
        ) {
            xy(0, 0)
        }

        image(AssetLoader.background) {
            smoothing = false
            size(width, height)
            xy(0.0, 0.0)
        }
    }

    private companion object {
        const val BACKGROUND_RED = 55
        const val BACKGROUND_GREEN = 80
        const val BACKGROUND_BLUE = 100
        const val BACKGROUND_ALPHA = 255
    }
}
