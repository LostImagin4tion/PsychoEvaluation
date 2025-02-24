plugins {
    conventions.`module-compose-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.api"
}

dependencies {
    implementation(project(":android:common:interactors:bleDeviceInteractor:api"))

    implementation(project(":android:feature:navigation:api"))
}
