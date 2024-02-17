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
    // ==== Feature layer ====

    // Navigation
    implementation(project(":android:feature:navigation:impl"))
}