package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.di.UsbDeviceRepositoryApiProvider

@Module(
    includes = [
        UsbDeviceRepositoryApiProvider::class
    ]
)
interface CoreAggregationModule
