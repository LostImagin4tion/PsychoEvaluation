plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainingPreparing.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))
    implementation(project(":android:common:interactors:bleDeviceInteractor:api"))
    implementation(project(":android:common:interactors:settingsInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:trainingPreparing:api"))

    implementation(project(":android:feature:navigation:api"))
}