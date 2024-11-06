import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl"
}

dependencies {

    Dependencies.GameEngine.allDeps.forEach { implementation(it) }

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))
    implementation(project(":android:common:interactors:bleDeviceInteractor:api"))
    implementation(project(":android:common:interactors:settingsInteractor:api"))
    implementation(project(":android:common:interactors:networkApi:statistics:api"))

    // ==== Feature layer ====

    api(project(":android:feature:trainings:stopwatchGame:api"))

    implementation(project(":android:feature:navigation:api"))
}
