plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.common.interactors.settingsInteractor.impl"
}

dependencies {

    // ==== Core layer ====

    implementation(project(":android:core:utils"))

    implementation(project(":android:core:dataStorage:api"))

    // ==== Common layer ====

    api(project(":android:common:interactors:settingsInteractor:api"))
}