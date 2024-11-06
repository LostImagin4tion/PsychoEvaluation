import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api"
}
dependencies {
    implementation(project(":multiplatform:core:networkApi:statistics"))

    Dependencies.ImmutableCollections.allDeps.forEach { implementation(it) }
}
