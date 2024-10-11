import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.statistics.impl"
}

dependencies {

    Dependencies.Calendar.allDeps.forEach { implementation(it) }

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    api(project(":android:feature:statistics:api"))

    implementation(project(":android:feature:navigation:api"))
    implementation(project(":android:common:interactors:networkApi:statistics:api"))
    implementation(project(":android:common:interactors:networkApi:statistics:api"))
    implementation(project(":android:core:dataStorage:api"))
}