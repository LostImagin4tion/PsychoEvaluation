import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-impl`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.dataStorage.impl"
}

dependencies {
    Dependencies.DataStore.allDeps.forEach { implementation(it) }

    // ==== Core layer ====
    api(project(":android:core:dataStorage:api"))
}
