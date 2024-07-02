plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl"
}

dependencies {

    // ==== Core layer ====

    implementation(project(":android:core:deviceApi:bleDeviceApi:api"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    api(project(":android:feature:bluetoothDeviceManager:api"))

    implementation(project(":android:feature:navigation:api"))
}