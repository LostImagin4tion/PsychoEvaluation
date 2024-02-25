plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.authorization.impl"
}

dependencies {

    // Common
    // Design system
    implementation(project(":android:common:designSystem"))

    // Feature
    // Authorization API
    implementation(project(":android:feature:authorization:api"))

    // Navigation API
    implementation(project(":android:feature:navigation:api"))
}