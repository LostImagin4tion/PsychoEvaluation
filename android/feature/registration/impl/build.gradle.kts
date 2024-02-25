plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.registration.impl"
}

dependencies {

    // Common
    // Design system
    implementation(project(":android:common:designSystem"))

    // Feature
    // Authorization API
    implementation(project(":android:feature:registration:api"))

    // Navigation API
    implementation(project(":android:feature:navigation:api"))
}