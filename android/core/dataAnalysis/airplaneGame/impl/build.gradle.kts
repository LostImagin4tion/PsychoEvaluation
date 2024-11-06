plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.impl"
}

dependencies {
    api(project(":android:core:dataAnalysis:airplaneGame:api"))
}