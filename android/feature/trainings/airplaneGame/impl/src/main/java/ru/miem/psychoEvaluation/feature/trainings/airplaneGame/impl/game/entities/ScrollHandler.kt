package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.entities

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import ru.miem.psychoEvaluation.common.designSystem.utils.dpd

fun Container.scrollHandler(
    screenWidth: Double,
    screenHeight: Double
) = ScrollHandler(screenWidth, screenHeight).addTo(this)

class ScrollHandler(
    private val screenWidth: Double,
    private val screenHeight: Double,
) : Container() {

    private val mountain1 = mountainView((28.5).dpd, screenHeight, scrollSpeed)
    private val mountain2 = mountainView(mountain1.rightmostX + mountainGap, screenHeight, scrollSpeed)

    private val clouds1 = cloudsView(60.dpd, 28.dpd, scrollSpeed)
    private val clouds2 = cloudsView(clouds1.rightmostX + cloudsGap, 28.dpd, scrollSpeed)
    private val clouds3 = cloudsView(clouds2.rightmostX + cloudsGap, 28.dpd, scrollSpeed)

    private val forest1X
        get() = -forestGap
    private val forest1 = forestView(forest1X, screenHeight, scrollSpeed)

    private val forest2X
        get() = (screenWidth / 2).coerceAtLeast(forest1.rightmostX) + forestGap
    private val forest2 = forestView(forest2X, screenHeight, scrollSpeed)

    private val forest3X
        get() = forest2.rightmostX + forestGap
    private val forest3 = forestView(forest3X, screenHeight, scrollSpeed)

    private val forest4X
        get() = forest3.rightmostX + screenWidth / 2 + forestGap
    private val forest4 = forestView(forest4X, screenHeight, scrollSpeed)

    private val rockLeft1X
        get() = -rockGap
    private val rockLeft1 = rockLeftView(rockLeft1X, screenHeight, scrollSpeed)

    private val rockRight1X
        get() = (screenWidth / 2).coerceAtLeast(rockLeft1.rightmostX) + rockGap
    private val rockRight1 = rockRightView(rockRight1X, screenHeight, scrollSpeed)

    private val rockLeft2X
        get() = rockRight1.rightmostX + rockGap
    private val rockLeft2 = rockLeftView(rockLeft2X, screenHeight, scrollSpeed)

    private val rockRight2X
        get() = rockLeft2.rightmostX + screenWidth / 2 + rockGap
    private val rockRight2 = rockRightView(rockRight2X, screenHeight, scrollSpeed)

    private val grassLeft1X
        get() = grassGap
    private val grassLeft1 = grassLeftView(grassLeft1X, screenHeight, scrollSpeed)

    private val grassMiddle1X
        get() = grassLeft1.rightmostX + grassGap
    private val grassMiddle1 = grassMiddleView(grassMiddle1X, screenHeight, scrollSpeed)

    private val grassRight1X
        get() = grassMiddle1.rightmostX + grassGap
    private val grassRight1 = grassRightView(grassRight1X, screenHeight, scrollSpeed)

    private val grassLeft2X
        get() = grassRight1.rightmostX + grassGap
    private val grassLeft2 = grassLeftView(grassLeft2X, screenHeight, scrollSpeed)

    private val grassMiddle2X
        get() = grassLeft2.rightmostX + grassGap
    private val grassMiddle2 = grassMiddleView(grassMiddle2X, screenHeight, scrollSpeed)

    private val grassRight2X
        get() = grassMiddle2.rightmostX + grassGap
    private val grassRight2 = grassRightView(grassRight2X, screenHeight, scrollSpeed)

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
            mountain1.isScrolledLeft -> mountain1.reset(mountain2.rightmostX + mountainGap)
            mountain2.isScrolledLeft -> mountain2.reset(mountain1.rightmostX + mountainGap)

            clouds1.isScrolledLeft -> clouds1.reset(clouds3.rightmostX + cloudsGap)
            clouds2.isScrolledLeft -> clouds2.reset(clouds1.rightmostX + cloudsGap)
            clouds3.isScrolledLeft -> clouds3.reset(clouds2.rightmostX + cloudsGap)

            forest1.isScrolledLeft -> forest1.reset(forest4.rightmostX + forestGap)
            forest2.isScrolledLeft -> forest2.reset(forest1.rightmostX + screenWidth / 2 + forestGap)
            forest3.isScrolledLeft -> forest3.reset(forest2.rightmostX + forestGap)
            forest4.isScrolledLeft -> forest4.reset(forest3.rightmostX + screenWidth / 2 + forestGap)

            rockLeft1.isScrolledLeft -> rockLeft1.reset(rockRight2.rightmostX + rockGap)
            rockRight1.isScrolledLeft -> rockRight1.reset(rockLeft1.rightmostX + screenWidth / 2 + rockGap)

            rockLeft2.isScrolledLeft -> rockLeft2.reset(rockRight1.rightmostX + rockGap)
            rockRight2.isScrolledLeft -> rockRight2.reset(rockLeft2.rightmostX + screenWidth / 2 + rockGap)

            grassLeft1.isScrolledLeft -> grassLeft1.reset(grassRight2.rightmostX + grassGap)
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
        mountain1.onRestart(0.0, scrollSpeed)
        mountain2.onRestart(mountain1.rightmostX + mountainGap, scrollSpeed)

        clouds1.onRestart(cloudDefaultX, scrollSpeed)
        clouds2.onRestart(clouds1.rightmostX + cloudsGap, scrollSpeed)
        clouds3.onRestart(clouds2.rightmostX + cloudsGap, scrollSpeed)

        forest1.onRestart(forest1X, scrollSpeed)
        forest2.onRestart(forest2X, scrollSpeed)

        forest3.onRestart(forest3X, scrollSpeed)
        forest4.onRestart(forest4X, scrollSpeed)

        rockLeft1.onRestart(rockLeft1X, scrollSpeed)
        rockRight1.onRestart(rockRight1X, scrollSpeed)

        rockLeft2.onRestart(rockLeft1X, scrollSpeed)
        rockRight2.onRestart(rockRight2X, scrollSpeed)

        grassLeft1.onRestart(grassLeft1X, scrollSpeed)
        grassMiddle1.onRestart(grassMiddle1X, scrollSpeed)
        grassRight1.onRestart(grassRight1X, scrollSpeed)

        grassLeft2.onRestart(grassLeft2X, scrollSpeed)
        grassMiddle2.onRestart(grassMiddle2X, scrollSpeed)
        grassRight2.onRestart(grassRight2X, scrollSpeed)
    }

    private companion object {
        val cloudDefaultX = 60.dpd

        val scrollSpeed = (-28.5).dpd

        val mountainGap = 0.0.dpd
        val cloudsGap = 85.dpd
        val forestGap = 28.dpd
        val rockGap = 28.dpd
        val grassGap = (-57).dpd
    }
}
