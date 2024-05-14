package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.circle
import com.soywiz.korge.view.image
import com.soywiz.korge.view.rotation
import com.soywiz.korge.view.xy
import com.soywiz.korge.view.zIndex
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.Point
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader

fun Container.airplane(
    x: Double,
    y: Double
) = Airplane(x = x, y = y).addTo(this)

class Airplane(
    x: Double,
    y: Double
) : Container() {

    private var isAlive = true
    private var airplaneRotationDegrees = 0.0

    private val position = Point(x, y) // todo: is there a vector2d class just to name it more suitable?
    private val velocity = Point()
    private val acceleration = Point(0, 920) // todo: how to make it immutable? (don't need it here but for the future)

    private val image = image(currentAirplaneImage, 0.5, 0.5) { smoothing = false }

    private val boundingCircle: Circle = circle(
        BOUNDING_RADIUS,
        Colors.TRANSPARENT_WHITE
    ).xy(-BOUNDING_RADIUS, -BOUNDING_RADIUS)

    val highestY
        get() = (position.y - BOUNDING_RADIUS)

    val lowestY
        get() = position.y + BOUNDING_RADIUS

    init {
        updateView()
        zIndex(Int.MAX_VALUE)
    }

    fun update(delta: TimeSpan) {
        velocity += acceleration * delta.seconds
        velocity.y = minOf(MIN_VELOCITY, velocity.y)

        if (position.y < 0) {
            position.y = 0.0
            velocity.y = 0.0
        }

        position += velocity * delta.seconds

        if (velocity.y < 0) {
            airplaneRotationDegrees -= FALLING_AIRPLANE_DEGREES * delta.seconds
            airplaneRotationDegrees = maxOf(MAX_FALLING_AIRPLANE_DEGREES, airplaneRotationDegrees)
        }

        if (isFalling || !isAlive) {
            airplaneRotationDegrees += LIVE_AIRPLANE_DEGREES * delta.seconds
            airplaneRotationDegrees = minOf(MIN_LIVE_AIRPLANE_DEGREES, airplaneRotationDegrees)
        }

        updateView()
    }

    private fun updateView() {
        this.xy(position.x, position.y)
        image.rotation(Angle.fromDegrees(airplaneRotationDegrees))
    }

    fun onClick() {
        if (isAlive) {
            velocity.y = VELOCITY_MODIFIER
        }
    }

    fun die() {
        isAlive = false
        velocity.y = 0.0
    }

    fun decelerate() {
        acceleration.y = 0.0
    }

    fun onRestart(y: Double) {
        airplaneRotationDegrees = 0.0
        position.y = y
        velocity.setTo(0.0, 0.0)
        acceleration.setTo(0.0, DEFAULT_ACCELERATION)
        isAlive = true
    }

    private val isFalling get() = velocity.y > 110

    private val currentAirplaneImage: Bitmap
        get() = AssetLoader.airplane

    private companion object {
        const val BOUNDING_RADIUS = 16.0
        const val MIN_VELOCITY = 200.0
        const val DEFAULT_ACCELERATION = 460.0
        const val FALLING_AIRPLANE_DEGREES = 600.0
        const val LIVE_AIRPLANE_DEGREES = 480.0
        const val MAX_FALLING_AIRPLANE_DEGREES = -20.0
        const val MIN_LIVE_AIRPLANE_DEGREES = 50.0
        const val VELOCITY_MODIFIER = -140.0
    }
}
