package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game.resources

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.font.BitmapFont
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

object AssetLoader {

    lateinit var background: Bitmap
        private set

    lateinit var airplane: Bitmap
        private set

    lateinit var clouds: Bitmap
        private set

    lateinit var text: BitmapFont
        private set

    lateinit var shadow: BitmapFont
        private set

    suspend fun load() {
        background = resourcesVfs["background.png"].readBitmap()
        airplane = resourcesVfs["airplane.png"].readBitmap()
        clouds = resourcesVfs["clouds.png"].readBitmap()

        text = resourcesVfs["text.fnt"].readBitmapFont()
        shadow = resourcesVfs["shadow.fnt"].readBitmapFont()
    }
}