plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.usbDeviceInteractor.impl"
}

dependencies {

    // ==== Core layer ====

    implementation(project(":android:core:utils"))

    implementation(project(":android:core:usbDeviceApi:api"))
    implementation(project(":android:core:dataAnalysis:airplaneGame:api"))

    // ==== Common layer ====

    api(project(":android:common:interactors:usbDeviceInteractor:api"))
}