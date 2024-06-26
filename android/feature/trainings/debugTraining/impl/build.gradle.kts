plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainings.debugTraining.impl"
}

dependencies {

    // ==== Core layer ====

    implementation(project(":android:core:deviceApi:api"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:trainings:debugTraining:api"))

    implementation(project(":android:feature:navigation:api"))
}