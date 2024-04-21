plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl"
}

dependencies {

    // Core
    implementation(project(":android:core:usbDeviceApi:api"))

    // Common
    // Design system
    implementation(project(":android:common:designSystem"))

    // Feature
    // Authorization API
    implementation(project(":android:feature:trainings:debugTraining:api"))

    // Navigation API
    implementation(project(":android:feature:navigation:api"))
}