import ru.miem.psychoEvaluation.consts.CompileVersions
import ru.miem.psychoEvaluation.consts.Dependencies

/**
 * Convention that extends [conventions.module-app] with Compose deps
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("conventions.module-app")
}

android {
    buildFeatures {
        compose = true
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
}