plugins {
    conventions.`module-compose-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainings.clocksGame.api"
}

dependencies {
    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))
    implementation(project(":android:common:interactors:bleDeviceInteractor:api"))

    implementation(project(":android:feature:navigation:api"))
}
