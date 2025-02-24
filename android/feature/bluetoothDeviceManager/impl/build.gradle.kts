import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl"
}

dependencies {

    Dependencies.ImmutableCollections.allDeps.forEach { implementation(it) }

    // ==== Core layer ====

    implementation(project(":android:core:deviceApi:bleDeviceApi:api"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))
    implementation(project(":android:common:interactors:bleDeviceInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:bluetoothDeviceManager:api"))

    implementation(project(":android:feature:navigation:api"))
    implementation(project(":android:feature:navigation:impl"))
}