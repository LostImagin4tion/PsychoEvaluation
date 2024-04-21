plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.trainingsList.impl"
}

dependencies {

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    api(project(":android:feature:trainingsList:api"))

    implementation(project(":android:feature:navigation:api"))
}