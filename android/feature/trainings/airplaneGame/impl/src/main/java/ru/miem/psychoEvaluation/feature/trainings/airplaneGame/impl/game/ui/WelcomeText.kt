package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.Typography

fun Container.welcomeText(
    x: Double,
    y: Double,
    textSize: Double = Typography.headlineTextSize,
) = WelcomeText(x, y, textSize).addTo(this)

class WelcomeText(
    x: Double,
    y: Double,
    textSize: Double,
) : Container() {

    init {
        text(
            text = "Touch me",
            textSize = textSize,
            font = AssetLoader.shadow,
        ) { xy(x - this.width / 2, y - textSize / 2) }

        text(
            text = "Touch me",
            textSize = textSize,
            font = AssetLoader.text,
        ) { xy(x - this.width / 2, y - textSize / 2) }

        text(
            text = "if you are ready to start",
            textSize = textSize,
            font = AssetLoader.shadow,
        ) { xy(x - this.width / 2, y + textSize / 2) }

        text(
            text = "if you are ready to start",
            textSize = textSize,
            font = AssetLoader.text,
        ) { xy(x - this.width / 2, y + textSize / 2) }
    }
}
