plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.impl"
}

dependencies {

    // ==== Core layer ====

    implementation(project(":android:core:utils"))

    implementation(project(":android:core:deviceApi:bleDeviceApi:api"))

    // ==== Common layer ====

    api(project(":android:common:interactors:bleDeviceInteractor:api"))
}