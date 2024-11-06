import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.impl"
}

dependencies {

    Dependencies.ImmutableCollections.allDeps.forEach { implementation(it) }
    Dependencies.Calendar.allDeps.forEach { implementation(it) }

    // ==== Multiplatform Core Layer ====

    implementation(project(":multiplatform:core:networkApi:statistics"))

    // ==== Core layer ====

    implementation(project(":android:core:utils"))
    implementation(project(":android:core:dataStorage:api"))

    // ==== Common layer ====

    api(project(":android:common:interactors:networkApi:statistics:api"))
    implementation(project(":android:common:interactors:networkApi:statistics:api"))
}