plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api"
}

dependencies {

    // ==== Multiplatform Core Layer ====

    implementation(project(":multiplatform:core:networkApi:authorization"))

    // ==== Core layer ====

    implementation(project(":android:core:utils"))
    implementation(project(":android:core:dataStorage:api"))

    // ==== Common layer ====

    api(project(":android:common:interactors:networkApi:authorization:api"))
}