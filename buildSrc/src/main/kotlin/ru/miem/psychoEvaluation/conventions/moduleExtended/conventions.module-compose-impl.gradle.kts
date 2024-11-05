import ru.miem.psychoEvaluation.consts.CompileVersions
import ru.miem.psychoEvaluation.consts.Dependencies

/**
 * Convention for extending [conventions.module-impl] to android-compose component
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("conventions.module-impl")
}

android {
    buildFeatures {
        compose = true
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = CompileVersions.COMPOSE_COMPILE_VERSION
    }
}

dependencies {

    // Android Core
    Dependencies.AndroidCore.allDeps.forEach { implementation(it) }

    // Compose
    implementation(platform(Dependencies.Compose.COMPOSE_BOM))
    Dependencies.Compose.allDeps.forEach { implementation(it) }
    Dependencies.Compose.allDebugDeps.forEach { debugImplementation(it) }

    // UI
    Dependencies.UI.allDeps.forEach { implementation(it) }
    Dependencies.Charts.allDeps.forEach { implementation(it) }
}