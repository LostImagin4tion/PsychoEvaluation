plugins {
    conventions.`module-compose-app`
}

android {
    namespace = "ru.miem.psychoEvaluation"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // ==== Core layer ====

    // DI
    implementation(project(":android:core:di:impl"))

    // USB Device Api
    implementation(project(":android:core:usbDeviceApi:impl"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    implementation(project(":android:feature:navigation:impl"))

    implementation(project(":android:feature:authorization:api"))
    implementation(project(":android:feature:authorization:impl"))

    implementation(project(":android:feature:registration:api"))
    implementation(project(":android:feature:registration:impl"))

    implementation(project(":android:feature:userProfile:api"))
    implementation(project(":android:feature:userProfile:impl"))

    implementation(project(":android:feature:trainingsList:api"))
    implementation(project(":android:feature:trainingsList:impl"))

    implementation(project(":android:feature:trainings:debugTraining:api"))
    implementation(project(":android:feature:trainings:debugTraining:impl"))
}