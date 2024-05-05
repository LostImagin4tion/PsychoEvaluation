package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import com.soywiz.korge.view.scale
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
        solidRect(
            width = width,
            height = height,
            color = RGBA(55, 80, 100, 255)
        ).xy(0, 0) // background

        image(AssetLoader.background) {
            smoothing = false
            scale(width / this.width, height / this.height)
//            scaleWhileMaintainingAspect(ScalingOption.ByWidthAndHeight(width, height))
        }.xy(0.0, 0.0) // todo: make bg move but slower than foreground
    }
}
