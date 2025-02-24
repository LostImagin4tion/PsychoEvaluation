package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.utils

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.container
import com.soywiz.korge.view.xy
import com.soywiz.korma.geom.Point

abstract class LeftScrollable(
    x: Double,
    y: Double,
    width: Int,
    scrollSpeed: Double
) : Container() {

    protected val scrollingContainer = container()

    private val myWidth = width

    protected val position = Point(x, y)
    private val velocity = Point(scrollSpeed, 0.0)

    val isScrolledLeft get() = rightmostX < 0

    init {
        updateView()
    }

    fun update(delta: TimeSpan) {
        position += velocity * delta.seconds
        updateView()
        onUpdate()
    }

    protected open fun onUpdate() {}

    private fun updateView() {
        scrollingContainer.xy(position.x, position.y)
    }

    fun stop() {
        velocity.x = 0.0
    }

    fun reset(x: Double) {
        position.x = x
        onReset()
    }

    protected open fun onReset() {}

    fun onRestart(x: Double, scrollSpeed: Double) {
        velocity.x = scrollSpeed
        reset(x)
    }

    val rightmostX get() = position.x + myWidth
}
