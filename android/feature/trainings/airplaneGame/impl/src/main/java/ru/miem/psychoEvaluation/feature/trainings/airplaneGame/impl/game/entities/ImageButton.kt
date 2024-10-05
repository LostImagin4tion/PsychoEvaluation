package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.korge.input.mouse
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import com.soywiz.korge.view.scale
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.Bitmap

fun Container.imageButton(
    image: Bitmap,
    width: Double,
    height: Double,
    onClick: () -> Unit,
) = ImageButton(image, width, height, onClick).addTo(this)

class ImageButton(
    image: Bitmap,
    width: Double,
    height: Double,
    onClick: () -> Unit,
) : Container() {

    init {
        image(
            image,
            anchorX = 0.5,
            anchorY = 0.5,
        ) {
            smoothing = false
            scale(width / this.width, height / this.height)
            xy(0.0, 0.0)
        }
        mouse {
            click {
                onClick()
            }
        }
    }
}
