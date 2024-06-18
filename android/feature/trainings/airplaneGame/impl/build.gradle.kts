import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainings.airplaneGame.impl"
}

dependencies {

    Dependencies.GameEngine.allDeps.forEach { implementation(it) }

    // ==== Core layer ====

    implementation(project(":android:core:deviceApi:api"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:trainings:airplaneGame:api"))

    implementation(project(":android:feature:navigation:api"))
}
