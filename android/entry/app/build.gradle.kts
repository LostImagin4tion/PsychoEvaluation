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

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    // Navigation
    implementation(project(":android:feature:navigation:impl"))
}