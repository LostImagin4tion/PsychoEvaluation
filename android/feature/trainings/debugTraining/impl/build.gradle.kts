plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl"
}

dependencies {

    // ==== Core layer ====

    implementation(project(":android:core:usbDeviceApi:api"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    api(project(":android:feature:trainings:debugTraining:api"))

    implementation(project(":android:feature:navigation:api"))
}