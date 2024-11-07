package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.image
import com.soywiz.korge.view.rotation
import com.soywiz.korge.view.size
import com.soywiz.korge.view.xy
import com.soywiz.korge.view.zIndex
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.Point
import ru.miem.psychoEvaluation.common.designSystem.utils.dpd
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import kotlin.math.sign

fun Container.airplaneView(
    x: Double,
    y: Double,
    lowestY: Double,
    highestY: Double,
) = AirplaneView(x, y, lowestY, highestY).addTo(this)

class AirplaneView(
    x: Double,
    y: Double,
    minY: Double,
    maxY: Double,
) : Container() {

    private var isAlive = true
    private var airplaneRotationDegrees = 0.0

    // todo: is there a vector2d class just to name it more suitable?
    private var position = Point(x, y)

    private val velocity = Point()
    private val acceleration = Point()

    private val airplane = image(AssetLoader.airplane, 0.5, 0.5) {
        smoothing = false
        size(airplaneWidth, airplaneHeight)
    }

    private val maxYPosition = maxY - airplaneHeight / 2
    private val minYPosition = minY + airplaneHeight / 2

    init {
        updateView()
        zIndex(Int.MAX_VALUE)
    }

    val highestY
        get() = position.y - airplaneHeight / 2

    val lowestY
        get() = position.y + airplaneHeight / 2

    fun update(delta: TimeSpan) {
        val positionRange = minYPosition..maxYPosition

        val newPosition = position + velocity * delta.seconds

        position = newPosition.run {
            copy(y = y.coerceIn(positionRange))
        }

        if (acceleration.y > 0) {
            airplaneRotationDegrees -= airplaneRotationDegreesDelta
            airplaneRotationDegrees = maxOf(-airplaneMaximumRotationDegrees, airplaneRotationDegrees)
        } else if (acceleration.y < 0) {
            airplaneRotationDegrees += airplaneRotationDegreesDelta
            airplaneRotationDegrees = minOf(airplaneMaximumRotationDegrees, airplaneRotationDegrees)
        } else {
            airplaneRotationDegrees = 0.0
        }

        updateView()
    }

    private fun updateView() {
        airplane.rotation(Angle.fromDegrees(airplaneRotationDegrees))
        xy(position.x, position.y)
    }

    fun onNewData(speed: Double) {
        if (isAlive) {
            acceleration.y = speed - velocity.y

            val additionalSpeed = speed * airplaneVelocityMultiplier

            if (position.y in minYPosition..maxYPosition || additionalSpeed.sign != velocity.y.sign) {
                velocity.y += speed * airplaneVelocityMultiplier
            }
        }
    }

    fun die() {
        isAlive = false
        velocity.y = 0.0
    }

    fun onStart() {
    }

    fun onDestroy() {
    }

    fun onRestart(y: Double) {
        airplaneRotationDegrees = 0.0
        position.y = y
        velocity.setTo(0.0, 0.0)
        acceleration.setTo(0.0, 0.0)
        isAlive = true
    }

    companion object {
        private val TAG = AirplaneView::class.java.simpleName

        private val boundingRadius = 31.dpd

        private val airplaneWidth = 182.dpd
        private val airplaneHeight = 59.dpd

        private val airplaneVelocityMultiplier = (-2.5).dpd

        private val airplaneRotationDegreesDelta = (0.071).dpd
        private val airplaneMaximumRotationDegrees = (4.3).dpd
    }
}
