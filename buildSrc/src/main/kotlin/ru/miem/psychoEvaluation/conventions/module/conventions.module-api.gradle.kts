import ru.miem.psychoEvaluation.consts.Dependencies

/**
 * Convention for api modules, which contains [conventions.base-api] and
 * pluggs in necessary dependencies
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("conventions.base-api")
}

dependencies {

    Dependencies.Coroutines.allDeps.forEach { implementation(it) }

    // Core
    // DI
    api(project(":android:core:di:api"))
}