import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.usbDeviceApi.impl"
}

dependencies {

    // Coroutines
    Dependencies.Coroutines.allDeps.forEach { implementation(it) }

    Dependencies.UsbDrivers.allDeps.forEach { implementation(it) }

    // Common libs
    // Dagger 2
    Dependencies.Dagger.implDeps.forEach { implementation(it) }
    Dependencies.Dagger.kaptDeps.forEach { kapt(it) }

    // Core layer
    // DI API
    api(project(":android:core:usbDeviceApi:api"))
}
