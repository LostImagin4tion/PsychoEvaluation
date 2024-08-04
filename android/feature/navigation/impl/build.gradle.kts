import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.navigation.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:api"))
    implementation(project(":android:common:interactors:bleDeviceInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:navigation:api"))

    // Routes destinations
    implementation(project(":android:feature:authorization:api"))
    implementation(project(":android:feature:registration:api"))
    implementation(project(":android:feature:userProfile:api"))
    implementation(project(":android:feature:settings:api"))
    implementation(project(":android:feature:bluetoothDeviceManager:api"))
    implementation(project(":android:feature:trainingPreparing:api"))
    implementation(project(":android:feature:trainingsList:api"))
    implementation(project(":android:feature:trainings:debugTraining:api"))
    implementation(project(":android:feature:trainings:airplaneGame:api"))
}