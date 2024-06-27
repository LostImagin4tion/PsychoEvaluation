plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainingsList.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))
    implementation(project(":android:common:interactors:settingsInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:trainingsList:api"))

    implementation(project(":android:feature:navigation:api"))
}