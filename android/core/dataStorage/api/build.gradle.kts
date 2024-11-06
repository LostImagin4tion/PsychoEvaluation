import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    conventions.`module-api`
}

android {
    namespace = "ru.miem.psychoEvaluation.core.dataStorage.api"
}

dependencies {
    Dependencies.DataStore.allDeps.forEach { implementation(it) }
}
