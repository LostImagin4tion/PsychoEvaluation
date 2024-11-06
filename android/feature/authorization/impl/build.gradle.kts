plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.authorization.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))
    implementation(project(":android:common:interactors:networkApi:authorization:api"))

    // ==== Feature layer ====

    // Authorization API
    api(project(":android:feature:authorization:api"))

    // Navigation API
    implementation(project(":android:feature:navigation:api"))
}