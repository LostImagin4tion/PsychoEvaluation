import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl"
}

dependencies {
    // Usb Drivers
    Dependencies.UsbDrivers.allDeps.forEach { implementation(it) }

    // ==== Core layer ====

    api(project(":android:core:deviceApi:usbDeviceApi:api"))
}
