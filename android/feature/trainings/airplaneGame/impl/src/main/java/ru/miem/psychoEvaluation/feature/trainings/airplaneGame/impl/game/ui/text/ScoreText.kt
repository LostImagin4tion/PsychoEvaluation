package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.Typography

fun Container.scoreText(
    fontSize: Double = Typography.bodyTextTextSize
) = ScoreText(fontSize).addTo(this)

class ScoreText(
    textSize: Double
) : Container() {

    private val scoreShadow = text(
        text = "",
        textSize = textSize,
        font = AssetLoader.shadow
    ) // todo: make own view for text with shadow

    private val scoreText = text(
        text = "",
        textSize = textSize,
        font = AssetLoader.text
    )

    fun updateScoreView(score: Int, x: Double, y: Double) {
        val scoreString = score.toString()
        val shadowX = x - SHADOW_MODIFIER * scoreString.length

        scoreShadow.apply { text = scoreString }.xy(shadowX, y)
        scoreText.apply { text = scoreString }.xy(shadowX + 1, y - 1)
    }

    private companion object {
        const val SHADOW_MODIFIER = 3
    }
}
