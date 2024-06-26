plugins {
    conventions.`module-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.api"
}

dependencies {
    api(project(":android:core:deviceApi:api"))
}