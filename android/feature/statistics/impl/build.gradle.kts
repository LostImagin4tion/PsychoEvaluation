import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-compose-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.feature.statistics.impl"
}

dependencies {

    Dependencies.ImmutableCollections.allDeps.forEach { implementation(it) }
    Dependencies.Calendar.allDeps.forEach { implementation(it) }

    implementation(project(":multiplatform:core:networkApi:statistics"))

    // ==== Common layer ====

    implementation(project(":android:common:designSystem"))

    // ==== Feature layer ====

    api(project(":android:feature:statistics:api"))

    implementation(project(":android:feature:navigation:api"))
    implementation(project(":android:common:interactors:networkApi:statistics:api"))
    implementation(project(":android:common:interactors:networkApi:statistics:api"))
    implementation(project(":android:core:dataStorage:api"))
}