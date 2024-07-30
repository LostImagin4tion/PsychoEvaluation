plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.authorization.impl"
    namespace = "ru.miem.psychoEvaluation.feature.registration.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))
    implementation(project(":android:common:interactors:networkApi:authorization:api"))
    implementation(project(":android:common:interactors:networkApi:registration:api"))

    // ==== Feature layer ====

    // Authorization API
    api(project(":android:feature:authorization:api"))

    // Registration API
    api(project(":android:feature:registration:api"))

    // Navigation API
    implementation(project(":android:feature:navigation:api"))
}