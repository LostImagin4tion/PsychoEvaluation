plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api"
}

dependencies {

    // ==== Multiplatform Core Layer ====

    implementation(project(":multiplatform:core:networkApi:registration"))

    // ==== Core layer ====

    implementation(project(":android:core:utils"))
    implementation(project(":android:core:dataStorage:api"))

    // ==== Common layer ====

    api(project(":android:common:interactors:networkApi:registration:api"))
    implementation(project(":android:common:interactors:networkApi:registration:api"))
}