plugins {
    conventions.`module-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api"
}
dependencies {
    implementation(project(":multiplatform:core:networkApi:statistics"))
}
