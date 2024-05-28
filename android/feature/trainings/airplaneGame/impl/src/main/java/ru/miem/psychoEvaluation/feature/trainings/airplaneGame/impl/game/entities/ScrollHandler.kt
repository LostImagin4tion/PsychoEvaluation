package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo

fun Container.scrollHandler(
    screenWidth: Double,
    screenHeight: Double
) = ScrollHandler(screenWidth, screenHeight).addTo(this)

class ScrollHandler(
    private val screenWidth: Double,
    private val screenHeight: Double,
) : Container() {

    private val mountain1 = mountainView(100.0, screenHeight, SCROLL_SPEED)
    private val mountain2 = mountainView(mountain1.rightmostX + MOUNTAIN_GAP, screenHeight, SCROLL_SPEED)

    private val clouds1 = cloudsView(210.0, 100.0, SCROLL_SPEED)
    private val clouds2 = cloudsView(clouds1.rightmostX + CLOUDS_GAP, 100.0, SCROLL_SPEED)
    private val clouds3 = cloudsView(clouds2.rightmostX + CLOUDS_GAP, 100.0, SCROLL_SPEED)

    private val forest1X
        get() = -FOREST_GAP
    private val forest1 = forestView(forest1X, screenHeight, SCROLL_SPEED)

    private val forest2X
        get() = (screenWidth / 2).coerceAtLeast(forest1.rightmostX) + FOREST_GAP
    private val forest2 = forestView(forest2X, screenHeight, SCROLL_SPEED)

    private val forest3X
        get() = forest2.rightmostX + FOREST_GAP
    private val forest3 = forestView(forest3X, screenHeight, SCROLL_SPEED)

    private val forest4X
        get() = forest3.rightmostX + screenWidth / 2 + FOREST_GAP
    private val forest4 = forestView(forest4X, screenHeight, SCROLL_SPEED)

    private val rockLeft1X
        get() = -ROCK_GAP
    private val rockLeft1 = rockLeftView(rockLeft1X, screenHeight, SCROLL_SPEED)

    private val rockRight1X
        get() = (screenWidth / 2).coerceAtLeast(rockLeft1.rightmostX) + ROCK_GAP
    private val rockRight1 = rockRightView(rockRight1X, screenHeight, SCROLL_SPEED)

    private val rockLeft2X
        get() = rockRight1.rightmostX + ROCK_GAP
    private val rockLeft2 = rockLeftView(rockLeft2X, screenHeight, SCROLL_SPEED)

    private val rockRight2X
        get() = rockLeft2.rightmostX + screenWidth / 2 + ROCK_GAP
    private val rockRight2 = rockRightView(rockRight2X, screenHeight, SCROLL_SPEED)

    private val grassLeft1X
        get() = GRASS_GAP
    private val grassLeft1 = grassLeftView(grassLeft1X, screenHeight, SCROLL_SPEED)

    private val grassMiddle1X
        get() = grassLeft1.rightmostX + GRASS_GAP
    private val grassMiddle1 = grassMiddleView(grassMiddle1X, screenHeight, SCROLL_SPEED)

    private val grassRight1X
        get() = grassMiddle1.rightmostX + GRASS_GAP
    private val grassRight1 = grassRightView(grassRight1X, screenHeight, SCROLL_SPEED)

    private val grassLeft2X
        get() = grassRight1.rightmostX + GRASS_GAP
    private val grassLeft2 = grassLeftView(grassLeft2X, screenHeight, SCROLL_SPEED)

    private val grassMiddle2X
        get() = grassLeft2.rightmostX + GRASS_GAP
    private val grassMiddle2 = grassMiddleView(grassMiddle2X, screenHeight, SCROLL_SPEED)

    private val grassRight2X
        get() = grassMiddle2.rightmostX + GRASS_GAP
    private val grassRight2 = grassRightView(grassRight2X, screenHeight, SCROLL_SPEED)

    fun update(delta: TimeSpan) {
        mountain1.update(delta)
        mountain2.update(delta)

        clouds1.update(delta)
        clouds2.update(delta)
        clouds3.update(delta)

        forest1.update(delta)
        forest2.update(delta)
        forest3.update(delta)
        forest4.update(delta)

        rockLeft1.update(delta)
        rockRight1.update(delta)

        rockLeft2.update(delta)
        rockRight2.update(delta)

        grassLeft1.update(delta)
        grassMiddle1.update(delta)
        grassRight1.update(delta)

        grassLeft2.update(delta)
        grassMiddle2.update(delta)
        grassRight2.update(delta)

        when {
            mountain1.isScrolledLeft -> mountain1.reset(mountain2.rightmostX + MOUNTAIN_GAP)
            mountain2.isScrolledLeft -> mountain2.reset(mountain1.rightmostX + MOUNTAIN_GAP)

            clouds1.isScrolledLeft -> clouds1.reset(clouds3.rightmostX + CLOUDS_GAP)
            clouds2.isScrolledLeft -> clouds2.reset(clouds1.rightmostX + CLOUDS_GAP)
            clouds3.isScrolledLeft -> clouds3.reset(clouds2.rightmostX + CLOUDS_GAP)

            forest1.isScrolledLeft -> forest1.reset(forest4.rightmostX + FOREST_GAP)
            forest2.isScrolledLeft -> forest2.reset(forest1.rightmostX + screenWidth / 2 + FOREST_GAP)
            forest3.isScrolledLeft -> forest3.reset(forest2.rightmostX + FOREST_GAP)
            forest4.isScrolledLeft -> forest4.reset(forest3.rightmostX + screenWidth / 2 + FOREST_GAP)

            rockLeft1.isScrolledLeft -> rockLeft1.reset(rockRight2.rightmostX + ROCK_GAP)
            rockRight1.isScrolledLeft -> rockRight1.reset(rockLeft1.rightmostX + screenWidth / 2 + ROCK_GAP)

            rockLeft2.isScrolledLeft -> rockLeft2.reset(rockRight1.rightmostX + ROCK_GAP)
            rockRight2.isScrolledLeft -> rockRight2.reset(rockLeft2.rightmostX + screenWidth / 2 + ROCK_GAP)

            grassLeft1.isScrolledLeft -> grassLeft1.reset(grassRight2.rightmostX + GRASS_GAP)
            grassMiddle1.isScrolledLeft -> grassMiddle1.reset(grassMiddle1X)
            grassRight1.isScrolledLeft -> grassRight1.reset(grassRight1X)

            grassLeft2.isScrolledLeft -> grassLeft2.reset(grassLeft2X)
            grassMiddle2.isScrolledLeft -> grassMiddle2.reset(grassMiddle2X)
            grassRight2.isScrolledLeft -> grassRight2.reset(grassRight2X)
        }
    }

    fun stop() {
        mountain1.stop()
        mountain2.stop()

        clouds1.stop()
        clouds2.stop()
        clouds3.stop()

        forest1.stop()
        forest2.stop()
        forest3.stop()
        forest4.stop()

        rockLeft1.stop()
        rockRight1.stop()

        rockLeft2.stop()
        rockRight2.stop()

        grassLeft1.stop()
        grassMiddle1.stop()
        grassRight1.stop()

        grassLeft2.stop()
        grassMiddle2.stop()
        grassRight2.stop()
    }

    fun onRestart() {
        mountain1.onRestart(0.0, SCROLL_SPEED)
        mountain2.onRestart(mountain1.rightmostX + MOUNTAIN_GAP, SCROLL_SPEED)

        clouds1.onRestart(CLOUD_DEFAULT_X, SCROLL_SPEED)
        clouds2.onRestart(clouds1.rightmostX + CLOUDS_GAP, SCROLL_SPEED)
        clouds3.onRestart(clouds2.rightmostX + CLOUDS_GAP, SCROLL_SPEED)

        forest1.onRestart(forest1X, SCROLL_SPEED)
        forest2.onRestart(forest2X, SCROLL_SPEED)

        forest3.onRestart(forest3X, SCROLL_SPEED)
        forest4.onRestart(forest4X, SCROLL_SPEED)

        rockLeft1.onRestart(rockLeft1X, SCROLL_SPEED)
        rockRight1.onRestart(rockRight1X, SCROLL_SPEED)

        rockLeft2.onRestart(rockLeft1X, SCROLL_SPEED)
        rockRight2.onRestart(rockRight2X, SCROLL_SPEED)

        grassLeft1.onRestart(grassLeft1X, SCROLL_SPEED)
        grassMiddle1.onRestart(grassMiddle1X, SCROLL_SPEED)
        grassRight1.onRestart(grassRight1X, SCROLL_SPEED)

        grassLeft2.onRestart(grassLeft2X, SCROLL_SPEED)
        grassMiddle2.onRestart(grassMiddle2X, SCROLL_SPEED)
        grassRight2.onRestart(grassRight2X, SCROLL_SPEED)
    }

    private companion object {
        const val CLOUD_DEFAULT_X = 210.0

        const val SCROLL_SPEED = -100.0

        const val MOUNTAIN_GAP = 0.0
        const val CLOUDS_GAP = 300
        const val FOREST_GAP = 100.0
        const val ROCK_GAP = 100.0
        const val GRASS_GAP = -200.0
    }
}
