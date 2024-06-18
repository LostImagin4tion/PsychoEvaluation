import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.deviceApi.bleDeviceApi.impl"
}

dependencies {
    // ==== Core layer ====
    api(project(":android:core:deviceApi:bleDeviceApi:api"))
}
