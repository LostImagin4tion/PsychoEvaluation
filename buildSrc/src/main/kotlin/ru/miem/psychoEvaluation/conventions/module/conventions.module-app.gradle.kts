import ru.miem.psychoEvaluation.consts.Dependencies

/**
 * Convention for app modules, which containts [conventions.base-app] and
 * pluggs in necessary dependencies
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("conventions.base-app")
}

dependencies {
    // Core
    Dependencies.AndroidCore.allDeps.forEach { implementation(it) }

    // UI
    Dependencies.UI.allDeps.forEach { implementation(it) }

    // Dagger 2
    Dependencies.Dagger.implDeps.forEach { implementation(it) }
    Dependencies.Dagger.kaptDeps.forEach { kapt(it) }

    // Logger
    Dependencies.Logger.allDeps.forEach { implementation(it) }
}

fun DependencyHandler.allTestImplementation(deps: Any) {
    testImplementation(deps)
    androidTestImplementation(deps)
}