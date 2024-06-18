plugins {
    conventions.`module-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.api"
}

dependencies {
    api(project(":android:core:deviceApi:api"))
}