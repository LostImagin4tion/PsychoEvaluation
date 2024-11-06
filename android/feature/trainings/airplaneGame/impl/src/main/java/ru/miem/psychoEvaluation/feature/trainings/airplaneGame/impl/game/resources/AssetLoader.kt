package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.font.BitmapFont
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

object AssetLoader {
    lateinit var airplane: Bitmap
        private set

    lateinit var background: Bitmap
        private set

    lateinit var clouds: Bitmap
        private set

    lateinit var forestLeft: Bitmap
        private set

    lateinit var grassLeft: Bitmap
        private set

    lateinit var grassMiddle: Bitmap
        private set

    lateinit var grassRight: Bitmap
        private set

    lateinit var mountain: Bitmap
        private set

    lateinit var rockLeft: Bitmap
        private set

    lateinit var rockRight: Bitmap
        private set

    lateinit var startButton: Bitmap
        private set

    lateinit var settingsButton: Bitmap
        private set

    lateinit var statisticsButton: Bitmap
        private set

    lateinit var exitButton: Bitmap
        private set

    lateinit var text: BitmapFont
        private set

    lateinit var shadow: BitmapFont
        private set

    suspend fun load() {
        airplane = resourcesVfs["airplane/airplane.png"].readBitmap()
        background = resourcesVfs["background/background.png"].readBitmap()
        clouds = resourcesVfs["clouds/clouds.png"].readBitmap()
        forestLeft = resourcesVfs["forest/forest.png"].readBitmap()
        grassLeft = resourcesVfs["grass/grass_left.png"].readBitmap()
        grassMiddle = resourcesVfs["grass/grass_middle.png"].readBitmap()
        grassRight = resourcesVfs["grass/grass_right.png"].readBitmap()
        mountain = resourcesVfs["mountain/mountain.png"].readBitmap()
        rockLeft = resourcesVfs["rock/rock_left.png"].readBitmap()
        rockRight = resourcesVfs["rock/rock_right.png"].readBitmap()

        startButton = resourcesVfs["buttons/start_button.png"].readBitmap()
        settingsButton = resourcesVfs["buttons/settings_button.png"].readBitmap()
        statisticsButton = resourcesVfs["buttons/statistics_button.png"].readBitmap()
        exitButton = resourcesVfs["buttons/exit_button.png"].readBitmap()

        text = resourcesVfs["text/text.fnt"].readBitmapFont()
        shadow = resourcesVfs["text/shadow.fnt"].readBitmapFont()
    }
}
