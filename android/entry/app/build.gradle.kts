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

    // ==== Core layer ====

    // DI
    implementation(project(":android:core:di:impl"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    // Navigation
    implementation(project(":android:feature:navigation:impl"))

    implementation(project(":android:feature:authorization:api"))
    implementation(project(":android:feature:authorization:impl"))

    implementation(project(":android:feature:registration:api"))
    implementation(project(":android:feature:registration:impl"))

    implementation(project(":android:feature:userProfile:api"))
    implementation(project(":android:feature:userProfile:impl"))
}