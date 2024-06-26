package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import android.content.Context
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.api.models.UsbDeviceData
import kotlin.reflect.KClass

class GameModule(
    private val screenWidth: Int,
    private val screenHeight: Int,
    private val context: Context,
    private val dataFlow: StateFlow<UsbDeviceData>
) : Module() {

    override val scaleMode: ScaleMode = ScaleMode.FILL
    override val size: SizeInt = SizeInt.invoke(screenWidth, screenHeight)
    override val windowSize = SizeInt.invoke(screenWidth, screenHeight)

    override val mainScene: KClass<out Scene> = GameScene::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype {
            GameScene(
                screenWidth = screenWidth.toDouble(),
                screenHeight = screenHeight.toDouble(),
                context = context,
                dataFlow = dataFlow,
            )
        }
    }
}
