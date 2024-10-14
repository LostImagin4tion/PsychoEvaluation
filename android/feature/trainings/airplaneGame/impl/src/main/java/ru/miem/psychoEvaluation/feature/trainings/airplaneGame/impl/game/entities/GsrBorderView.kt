package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.RGBA

fun Container.gsrBorderView(
    width: Double,
    height: Double,
) = GsrBorderView(width, height).addTo(this)

class GsrBorderView(
    width: Double,
    height: Double,
) : Container() {

    init {
        solidRect(
            width = width,
            height = height,
            color = RGBA(COLOR_RED, COLOR_GREEN, COLOR_BLUE, COLOR_ALPHA),
        )
    }

    private companion object {
        const val COLOR_RED = 0
        const val COLOR_GREEN = 0
        const val COLOR_BLUE = 0
        const val COLOR_ALPHA = 255
    }
}
