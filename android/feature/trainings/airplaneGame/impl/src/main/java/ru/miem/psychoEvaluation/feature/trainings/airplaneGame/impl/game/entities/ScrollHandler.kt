package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo

fun Container.scrollHandler() = ScrollHandler().addTo(this)

class ScrollHandler : Container() {

    private val clouds1 = clouds(210.0, 0.0, SCROLL_SPEED)
    private val clouds2 = clouds(clouds1.rightmostX + CLOUDS_GAP, 0.0, SCROLL_SPEED)
    private val clouds3 = clouds(clouds2.rightmostX + CLOUDS_GAP, 0.0, SCROLL_SPEED)

    fun update(delta: TimeSpan) {
        clouds1.update(delta)
        clouds2.update(delta)
        clouds3.update(delta)

        when {
            clouds1.isScrolledLeft -> clouds1.reset(clouds3.rightmostX + CLOUDS_GAP)
            clouds2.isScrolledLeft -> clouds2.reset(clouds1.rightmostX + CLOUDS_GAP)
            clouds3.isScrolledLeft -> clouds3.reset(clouds2.rightmostX + CLOUDS_GAP)
        }
    }

    fun stop() {
        clouds1.stop()
        clouds2.stop()
        clouds3.stop()
    }

    fun onRestart() {
        clouds1.onRestart(CLOUD_DEFAULT_X, SCROLL_SPEED)
        clouds2.onRestart(clouds1.rightmostX + CLOUDS_GAP, SCROLL_SPEED)
        clouds3.onRestart(clouds2.rightmostX + CLOUDS_GAP, SCROLL_SPEED)
    }

    private companion object {
        const val CLOUD_DEFAULT_X = 210.0

        const val SCROLL_SPEED = -100.0
        const val CLOUDS_GAP = 300
    }
}
