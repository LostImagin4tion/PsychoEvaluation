package ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl.game

import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlinx.coroutines.flow.StateFlow
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.api.models.UsbDeviceData
import kotlin.reflect.KClass

class CustomModule(
    private val width: Int = DEFAULT_WIDTH,
    private val height: Int = DEFAULT_HEIGHT,
    private val dataFlow: StateFlow<UsbDeviceData>
) : Module() {

    companion object {
        const val DEFAULT_WIDTH = 1920
        const val DEFAULT_HEIGHT = 1080
    }

    override val size: SizeInt
        get() = SizeInt.invoke(width, height)

    override val windowSize: SizeInt
        get() = SizeInt.invoke(width, height)

    override val mainScene: KClass<out Scene> = CustomScene::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype {
            CustomScene(
                widthParam = width.toDouble(),
                heightParam = height.toDouble(),
                dataFlow = dataFlow,
            )
        }
    }
}
