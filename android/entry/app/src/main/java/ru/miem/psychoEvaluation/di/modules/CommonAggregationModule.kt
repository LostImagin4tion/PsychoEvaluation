package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractors.impl.di.UsbDeviceInteractorApiProvider
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.di.DataAnalysisApiProvider

@Module(
    includes = [
        DataAnalysisApiProvider::class,
        UsbDeviceInteractorApiProvider::class,
    ]
)
interface CommonAggregationModule
