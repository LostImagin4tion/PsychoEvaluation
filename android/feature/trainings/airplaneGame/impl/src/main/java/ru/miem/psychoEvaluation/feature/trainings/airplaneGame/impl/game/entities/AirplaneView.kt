package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import android.content.Context
import android.os.Environment
import android.widget.Toast
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
import ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources.AssetLoader
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Container.airplaneView(
    x: Double,
    y: Double
) = AirplaneView(x, y).addTo(this)

class AirplaneView(
    x: Double,
    y: Double
) : Container() {

    private var isAlive = true
    private var airplaneRotationDegrees = 0.0

    private val position = Point(x, y) // todo: is there a vector2d class just to name it more suitable?
    private val velocity = Point()
    private val acceleration = Point()
    private val gravityAcceleration = Point(0.0, 1.0) // todo: how to make it immutable? (don't need it here but for the future)

    private val image = image(AssetLoader.airplane, 0.5, 0.5) {
        smoothing = false
        size(AIRPLANE_WIDTH_PX, AIRPLANE_HEIGHT_PX)
    }

    private val calendar = Calendar.getInstance()
    private var fileOutputWriter: BufferedWriter? = null

    val highestY
        get() = (position.y - BOUNDING_RADIUS)

    val lowestY
        get() = position.y + BOUNDING_RADIUS

    init {
        updateView()
        zIndex(Int.MAX_VALUE)
    }

    fun update(delta: TimeSpan) {
//        velocity += acceleration * delta.seconds
//        velocity.y = minOf(10.0, velocity.y)

        if (position.y < 0) {
            position.y = 0.0
            velocity.y = 0.0
        }

        position += velocity * delta.seconds

        if (acceleration.y > 0) {
            airplaneRotationDegrees -= 0.5
            airplaneRotationDegrees = maxOf(-15.0, airplaneRotationDegrees)
        }
        else if (acceleration.y < 0) {
            airplaneRotationDegrees += 0.5
            airplaneRotationDegrees = minOf(15.0, airplaneRotationDegrees)
        } else {
            airplaneRotationDegrees = 0.0
        }

        updateView()
    }

    private fun updateView() {
        this.xy(position.x, position.y)
        image.rotation(Angle.fromDegrees(airplaneRotationDegrees))
    }

    fun onNewData(rawData: Int, speed: Double) {
        if (isAlive) {
            acceleration.y = speed - velocity.y
            velocity.y = speed * -5.0
            Timber.tag("HELLO").i("velocity ${velocity.y} acceleration ${acceleration.y}")
            fileOutputWriter?.write("$rawData\n")
        }
    }

    fun die() {
        isAlive = false
        velocity.y = 0.0
        closeStream()
    }

    fun decelerate() {
        gravityAcceleration.y = 0.0
    }

    fun onStart(context: Context) {
        setupFileInputStream(context)
    }

    fun onDestroy() {
        closeStream()
    }

    fun onRestart(y: Double, context: Context) {
        setupFileInputStream(context)
        airplaneRotationDegrees = 0.0
        position.y = y
        velocity.setTo(0.0, 0.0)
        acceleration.setTo(0.0, 0.0)
        gravityAcceleration.setTo(0.0, 460.0)
        isAlive = true
    }

    private fun setupFileInputStream(context: Context) {
        try {
            closeStream()

            val datetime = calendar.time.toString("yyyy-MM-dd_HH:mm:ss")
            val filename = "$datetime+psycho.txt"

            Timber.tag(TAG).i("HELLO external files ${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}")

            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            fileOutputWriter = file.bufferedWriter()

//            fileOutputWriter = context
//                .openFileOutput(filename, Context.MODE_PRIVATE)
//                .bufferedWriter()

            Timber.tag(TAG).i(
                "Created new file ${file.absolutePath}, " +
                        "all files: ${file.parentFile?.listFiles()?.map { it.name }}"
            )
        } catch (e: IOException) {
            Timber.tag(TAG).e("Got IO error while writing data to file: $e ${e.message}")
        }
    }

    private fun closeStream() {
        fileOutputWriter?.let {
            it.close()
            Timber.tag(TAG).i("Closed file output writer")
        }
        fileOutputWriter = null
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    companion object {
        private val TAG = AirplaneView::class.java.simpleName

        private const val BOUNDING_RADIUS = 150.0

        private const val AIRPLANE_WIDTH_PX = 637.5
        private const val AIRPLANE_HEIGHT_PX = 207.0
    }
}
