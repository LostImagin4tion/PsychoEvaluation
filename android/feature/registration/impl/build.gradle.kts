plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.registration.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    api(project(":android:feature:registration:api"))

    implementation(project(":android:feature:navigation:api"))
}