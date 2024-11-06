plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.registration.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))
    implementation(project(":android:common:interactors:networkApi:registration:api"))

    // ==== Feature layer ====

    api(project(":android:feature:registration:api"))

    implementation(project(":android:feature:navigation:api"))
}