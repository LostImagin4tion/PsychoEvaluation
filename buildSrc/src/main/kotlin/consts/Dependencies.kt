/**
 * [Dependencies] is designed to store all the dependencies that are needed in this project
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
object Dependencies {

    object AndroidCore {
        private const val APPCOMPAT_VERSION = "1.6.1"
        private const val CORE_VERSION = "1.12.0"

        const val APPCOMPAT = "androidx.appcompat:appcompat:$APPCOMPAT_VERSION"
        const val CORE = "androidx.core:core-ktx:$CORE_VERSION"

        val allDeps = listOf(APPCOMPAT, CORE)
    }

    object Coroutines {
        private const val CORE_VERSION = "1.8.0"
        private const val ANDROID_VERSION = "1.3.9"

        const val CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$CORE_VERSION"
        const val ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$ANDROID_VERSION"

        val allDeps = listOf(CORE, ANDROID)
    }

    object Compose {

        // ===========================================================================================
        // BE VERY CAREFUL WHEN UPDATING COMPOSE DEPENDENCIES, BECAUSE THEY CAN USE DIFFERENT VERSION!
        // ===========================================================================================

        const val COMPOSE_BOM = "androidx.compose:compose-bom:2024.02.00"

        object Core {
            private const val ACTIVITY_COMPOSE_VERSION = "1.8.2"

            const val ACTIVITY_COMPOSE =
                "androidx.activity:activity-compose:$ACTIVITY_COMPOSE_VERSION"

            const val UI = "androidx.compose.ui:ui"
            const val UI_UTILS = "androidx.compose.ui:ui-util"

            const val UI_TOOLING = "androidx.compose.ui:ui-tooling" // debugImplementation
            const val UI_TOOLING_DATA = "androidx.compose.ui:ui-tooling-data"
            const val UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview"

            val allCoreDeps = listOf(ACTIVITY_COMPOSE, UI, UI_UTILS, UI_TOOLING, UI_TOOLING_DATA,
                UI_TOOLING_PREVIEW)
        }

        object Navigation {
            private const val VERSION = "2.7.7"

            const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:$VERSION"

            val allNavigationDeps = listOf(NAVIGATION_COMPOSE)
        }

        object Foundation {
            const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation"
            const val COMPOSE_FOUNDATION_LAYOUT = "androidx.compose.foundation:foundation-layout"

            val allFoundationDeps = listOf(COMPOSE_FOUNDATION, COMPOSE_FOUNDATION_LAYOUT)
        }

        object Material {
            const val MATERIAL = "androidx.compose.material3:material3"
            const val MATERIAL_ICONS = "androidx.compose.material:material-icons-extended"

            val allMaterialDeps = listOf(MATERIAL, MATERIAL_ICONS)
        }

        object Lifecycle {
            private const val LIFECYCLE_VERSION = "2.7.0"

            const val RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
            const val RUNTIME_COMPOSE =
                "androidx.lifecycle:lifecycle-runtime-compose:$LIFECYCLE_VERSION"
            const val VIEW_MODEL =
                "androidx.lifecycle:lifecycle-viewmodel-compose:$LIFECYCLE_VERSION"
            const val VIEW_MODEL_SAVED_STATE =
                "androidx.lifecycle:lifecycle-viewmodel-savedstate:$LIFECYCLE_VERSION"

            val allLifecycleDeps = listOf(RUNTIME, VIEW_MODEL, RUNTIME_COMPOSE,
                VIEW_MODEL_SAVED_STATE)
        }

        object Coil {
            private const val VERSION = "2.5.0"

            const val COIL_COMPOSE = "io.coil-kt:coil-compose:$VERSION"

            val allCoilDeps = listOf(COIL_COMPOSE)
        }

        val allDeps = Core.allCoreDeps + Navigation.allNavigationDeps +
                Foundation.allFoundationDeps + Material.allMaterialDeps +
                Lifecycle.allLifecycleDeps + Coil.allCoilDeps
    }

    object Redux {
        private const val VERSION = "0.5.5"

        const val MULTIPLATFORM = "org.reduxkotlin:redux-kotlin-threadsafe:$VERSION"
    }

    object Dagger {
        private const val VERSION = "2.50"

        const val DAGGER = "com.google.dagger:dagger$VERSION"
        const val ANDROID = "com.google.dagger:dagger-android:$VERSION"

        const val COMPILER = "com.google.dagger:dagger-compiler:$VERSION"
        const val PROCESSOR = "com.google.dagger:dagger-android-processor:$VERSION"

        val implDeps = listOf(DAGGER, ANDROID)
        val kspDeps = listOf(COMPILER, PROCESSOR)
    }

    object Logger {
        private const val TIMBER_VERSION = "5.0.1"

        const val TIMBER = "com.jakewharton.timber:timber:$TIMBER_VERSION"

        val allDeps = listOf(TIMBER)
    }

    object Network {
        private const val MOSHI_KOTLIN_VERSION = "1.15.0"
        private const val OKHTTP_VERSION = "4.12.0"

        const val MOSHI_KOTLIN = "com.squareup.moshi:moshi-kotlin:$MOSHI_KOTLIN_VERSION"

        const val OKHTTP = "com.squareup.okhttp3:okhttp:$OKHTTP_VERSION"
        const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:$OKHTTP_VERSION"

        const val MOSHI_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI_KOTLIN_VERSION"

        val allDeps = listOf(MOSHI_KOTLIN, OKHTTP, LOGGING_INTERCEPTOR)
        val allKspDeps = listOf(MOSHI_CODEGEN)
    }

    object Plugins {
        const val DETEKT_VERSION = "1.23.5"

        const val DETEKT_FORMATTING =
            "io.gitlab.arturbosch.detekt:detekt-formatting:$DETEKT_VERSION"
    }
}
