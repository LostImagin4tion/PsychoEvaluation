package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.ui.text

import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.Typography
import kotlin.time.Duration

fun Container.gameTimeText(
    x: Double,
    y: Double,
    textSize: Double = Typography.bodyTextTextSize,
) = GameTimeText(x, y, textSize).addTo(this)

class GameTimeText(
    x: Double,
    y: Double,
    textSize: Double,
) : Container() {

    private val gameTimeShadow = text(
        text = "00:00",
        textSize = textSize,
        font = AssetLoader.shadow,
    ) { xy(x - this.width / 2, y - textSize / 2) }

    private val gameTime = text(
        text = "00:00",
        textSize = textSize,
        font = AssetLoader.text,
    ) { xy(x - this.width / 2, y - textSize / 2) }

    @Suppress("MagicNumber")
    fun changeTime(time: Duration) {
        val minutes = time.inWholeMinutes.toInt()
        val seconds = time.inWholeSeconds.toInt() - minutes * 60

        val timeText = when {
            minutes >= 10 && seconds >= 10 -> "$minutes:$seconds"
            minutes < 10 && seconds >= 10 -> "0$minutes:$seconds"
            minutes >= 10 && seconds < 10 -> "$minutes:0$seconds"
            else -> "0$minutes:0$seconds"
        }

        gameTimeShadow.text = timeText
        gameTime.text = timeText
    }
}
