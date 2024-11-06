plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.settings.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))
    implementation(project(":android:common:interactors:settingsInteractor:api"))

    // ==== Feature layer ====

    api(project(":android:feature:settings:api"))

    implementation(project(":android:feature:navigation:api"))
}