package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.Typography

fun Container.gameOverText(
    x: Double,
    y: Double,
    textSize: Double = Typography.bodyTextTextSize
) = GameOverText(x, y, textSize).addTo(this)

class GameOverText(
    x: Double,
    y: Double,
    textSize: Double
) : Container() {

    init {
        text(
            text = "Game Over",
            textSize = textSize,
            font = AssetLoader.shadow
        ) { xy(x - this.width / 2, y - textSize / 2) }

        text(
            text = "Game Over",
            textSize = textSize,
            font = AssetLoader.text
        ) { xy(x - this.width / 2, y - textSize / 2) }

        text(
            text = "Try again?",
            textSize = textSize,
            font = AssetLoader.shadow
        ) { xy(x - this.width / 2, y + textSize / 2) }

        text(
            text = "Try again?",
            textSize = textSize,
            font = AssetLoader.text
        ) { xy(x - this.width / 2, y + textSize / 2) }
    }
}
