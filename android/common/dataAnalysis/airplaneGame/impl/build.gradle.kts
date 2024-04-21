plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.dataAnalysis.airplaneGame.impl"
}

dependencies {
    api(project(":android:common:dataAnalysis:airplaneGame:api"))
}