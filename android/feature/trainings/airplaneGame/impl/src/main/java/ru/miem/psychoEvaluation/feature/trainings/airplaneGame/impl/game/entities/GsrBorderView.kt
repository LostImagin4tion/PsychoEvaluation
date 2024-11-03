package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.RGBA

fun Container.gsrBorderView(
    width: Double,
    height: Double,
    color: RGBA,
) = GsrBorderView(width, height, color).addTo(this)

class GsrBorderView(
    width: Double,
    height: Double,
    color: RGBA,
) : Container() {

    init {
        solidRect(
            width = width,
            height = height,
            color = color,
        )
    }
}
