package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import com.soywiz.korge.view.*
import com.soywiz.korim.color.RGBA
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.helpers.AssetLoader

fun Container.backgroundView(
    width: Double,
    height: Double,
    midPointY: Double,
) = BackgroundView(width, height, midPointY).addTo(this)

class BackgroundView(
    width: Double,
    height: Double,
    midPointY: Double,
) : Container() {

    init {
        solidRect(
            width = width,
            height = midPointY + 66,
            color = RGBA(55, 80, 100, 255)
        ).xy(0, 0)  // background

        solidRect(
            width = width,
            height = 11.0,
            color = RGBA(111, 186, 45, 255)
        ).xy(0.0, midPointY + 66)  // grass

        solidRect(
            width = width,
            height = 52.0,
            color = RGBA(147, 80, 27, 255)
        ).xy(0.0, midPointY + 77)  // dirt

        image(AssetLoader.bg) { smoothing = false }
            .xy(0.0, midPointY + 23)  // todo: make bg move but slower than foreground
    }
}