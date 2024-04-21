plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainingsList.impl"
}

dependencies {

    // Common
    // Design system
    implementation(project(":android:common:designSystem"))

    // Feature
    // Authorization API
    implementation(project(":android:feature:trainingsList:api"))

    // Navigation API
    implementation(project(":android:feature:navigation:api"))
}