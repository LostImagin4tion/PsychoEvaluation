plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.navigation.impl"
}

dependencies {
    implementation(project(":android:common:designSystem"))

    implementation(project(":android:feature:navigation:api"))

    implementation(project(":android:feature:authorization:api"))
    implementation(project(":android:feature:registration:api"))
}