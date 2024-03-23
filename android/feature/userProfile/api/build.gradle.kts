plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.userProfile.api"
}

dependencies {
    implementation(project(":android:common:designSystem"))
}