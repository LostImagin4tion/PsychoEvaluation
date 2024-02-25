import org.gradle.kotlin.dsl.dependencies
import ru.miem.psychoEvaluation.consts.Dependencies

/**
 * Convention for impl modules, which containts [conventions.base-api] and
 * pluggs in necessary dependencies
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("conventions.base-impl")
}

dependencies {
    // Dagger 2
    Dependencies.Dagger.implDeps.forEach { implementation(it) }
    Dependencies.Dagger.kaptDeps.forEach { kapt(it) }

    // Coroutines
    Dependencies.Coroutines.allDeps.forEach { implementation(it) }

    // Logger
    Dependencies.Logger.allDeps.forEach { implementation(it) }

    // DI
    implementation(project(":android:core:di:impl"))
}

fun DependencyHandler.allTestImplementation(deps: Any) {
    testImplementation(deps)
    androidTestImplementation(deps)
}