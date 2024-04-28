import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.usbDeviceApi.impl"
}

dependencies {
    // Usb Drivers
    Dependencies.UsbDrivers.allDeps.forEach { implementation(it) }

    // ==== Core layer ====

    api(project(":android:core:usbDeviceApi:api"))
}
