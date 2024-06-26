import ru.miem.psychoEvaluation.consts.Dependencies

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

    Dependencies.DataStore.allDeps.forEach { implementation(it) }

    // ==== Core layer ====

    implementation(project(":android:core:di:impl"))
    implementation(project(":android:core:dataStorage:impl"))
    implementation(project(":android:core:deviceApi:usbDeviceApi:impl"))
    implementation(project(":android:core:deviceApi:bleDeviceApi:impl"))
    implementation(project(":android:core:dataAnalysis:airplaneGame:impl"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    implementation(project(":android:common:interactors:usbDeviceInteractor:impl"))
    implementation(project(":android:common:interactors:settingsInteractor:impl"))

    // ==== Feature layer ====

    implementation(project(":android:feature:navigation:impl"))
    implementation(project(":android:feature:authorization:impl"))
    implementation(project(":android:feature:registration:impl"))
    implementation(project(":android:feature:userProfile:impl"))
    implementation(project(":android:feature:settings:impl"))
    implementation(project(":android:feature:trainingsList:impl"))
    implementation(project(":android:feature:trainings:debugTraining:impl"))
    implementation(project(":android:feature:trainings:airplaneGame:impl"))
}
