package ru.miem.psychoEvaluation.consts

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
        private const val ANNOTATIONS_VERSION = "1.7.1"

        private const val APPCOMPAT = "androidx.appcompat:appcompat:$APPCOMPAT_VERSION"
        private const val CORE = "androidx.core:core-ktx:$CORE_VERSION"

        private const val ANNOTATIONS = "androidx.annotation:annotation:$ANNOTATIONS_VERSION"

        val allDeps = listOf(APPCOMPAT, CORE, ANNOTATIONS)
    }

    object Coroutines {
        private const val CORE_VERSION = "1.8.0"
        private const val ANDROID_VERSION = "1.3.9"

        private const val CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$CORE_VERSION"
        private const val ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$ANDROID_VERSION"

        val allDeps = listOf(CORE, ANDROID)
    }

    object Compose {

        // ===========================================================================================
        // BE VERY CAREFUL WHEN UPDATING COMPOSE DEPENDENCIES, BECAUSE THEY CAN USE DIFFERENT VERSION!
        // ===========================================================================================

        const val COMPOSE_BOM = "androidx.compose:compose-bom:2024.02.02"

        object Core {
            private const val IMMUTABLE_COLLECTIONS_VERSION = "0.3.7"

            private const val UI = "androidx.compose.ui:ui"
            private const val UI_UTILS = "androidx.compose.ui:ui-util"
            private const val IMMUTABLE_COLLECTIONS =
                "org.jetbrains.kotlinx:kotlinx-collections-immutable:$IMMUTABLE_COLLECTIONS_VERSION"

            val allCoreDeps = listOf(UI, UI_UTILS, IMMUTABLE_COLLECTIONS)
        }

        object Tooling {
            private const val UI_TOOLING = "androidx.compose.ui:ui-tooling" // debugImplementation
            private const val UI_TOOLING_DATA = "androidx.compose.ui:ui-tooling-data"
            private const val UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview"

            val allToolingDeps = listOf(UI_TOOLING_DATA, UI_TOOLING_PREVIEW)
            val allToolingDebugDeps = listOf(UI_TOOLING)
        }

        object Foundation {
            private const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation"
            private const val COMPOSE_FOUNDATION_LAYOUT = "androidx.compose.foundation:foundation-layout"

            val allFoundationDeps = listOf(COMPOSE_FOUNDATION, COMPOSE_FOUNDATION_LAYOUT)
        }

        object Material {
            private const val MATERIAL = "androidx.compose.material3:material3:1.1.0"
            private const val MATERIAL_ICONS = "androidx.compose.material:material-icons-extended"
            private const val MATERIALX = "androidx.compose.material:material"

            val allMaterialDeps = listOf(MATERIAL, MATERIALX, MATERIAL_ICONS)
        }

        object Lifecycle {
            private const val LIFECYCLE_VERSION = "2.7.0"
            private const val ACTIVITY_COMPOSE_VERSION = "1.8.2"

            private const val ACTIVITY_COMPOSE =
                "androidx.activity:activity-compose:$ACTIVITY_COMPOSE_VERSION"

            private const val RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
            private const val RUNTIME_COMPOSE =
                "androidx.lifecycle:lifecycle-runtime-compose:$LIFECYCLE_VERSION"
            private const val VIEW_MODEL =
                "androidx.lifecycle:lifecycle-viewmodel-compose:$LIFECYCLE_VERSION"
            private const val VIEW_MODEL_SAVED_STATE =
                "androidx.lifecycle:lifecycle-viewmodel-savedstate:$LIFECYCLE_VERSION"

            val allLifecycleDeps = listOf(
                ACTIVITY_COMPOSE, RUNTIME, VIEW_MODEL, RUNTIME_COMPOSE,
                VIEW_MODEL_SAVED_STATE
            )
        }

        object Navigation {
            private const val VERSION = "2.7.7"

            private const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:$VERSION"

            val allNavigationDeps = listOf(NAVIGATION_COMPOSE)
        }

        object Coil {
            private const val VERSION = "2.5.0"

            private const val COIL_COMPOSE = "io.coil-kt:coil-compose:$VERSION"

            val allCoilDeps = listOf(COIL_COMPOSE)
        }

        object Accompanist {
            private const val VERSION = "0.34.0"

            private const val PERMISSIONS = "com.google.accompanist:accompanist-permissions:$VERSION"

            val allDeps = listOf(PERMISSIONS)
        }

        val allDeps = Core.allCoreDeps + Tooling.allToolingDeps + Navigation.allNavigationDeps +
            Foundation.allFoundationDeps + Material.allMaterialDeps +
            Lifecycle.allLifecycleDeps + Coil.allCoilDeps + Accompanist.allDeps

        val allDebugDeps = Tooling.allToolingDebugDeps
    }

    object Redux {
        private const val VERSION = "0.5.5"

        private const val MULTIPLATFORM = "org.reduxkotlin:redux-kotlin-threadsafe:$VERSION"

        val allDeps = listOf(MULTIPLATFORM)
    }

    object Dagger {
        private const val VERSION = "2.50"

        private const val DAGGER = "com.google.dagger:dagger:$VERSION"
        private const val ANDROID = "com.google.dagger:dagger-android:$VERSION"

        private const val COMPILER = "com.google.dagger:dagger-compiler:$VERSION"
        private const val PROCESSOR = "com.google.dagger:dagger-android-processor:$VERSION"

        val implDeps = listOf(DAGGER, ANDROID)
        val kaptDeps = listOf(COMPILER, PROCESSOR)
    }

    object Logger {
        private const val TIMBER_VERSION = "5.0.1"

        private const val TIMBER = "com.jakewharton.timber:timber:$TIMBER_VERSION"

        val allDeps = listOf(TIMBER)
    }

    object Network {
        private const val KTOR_VERSION = "2.3.11"
        private const val LOGGING_INTERCEPTOR_VERSION = "4.12.0"
        private const val KOTLIN_SERIALIZATION_VERSION = "1.6.3"

        private const val KTOR_CORE = "io.ktor:ktor-client-core:$KTOR_VERSION"
        private const val KTOR_OKHTTP = "io.ktor:ktor-client-okhttp:$KTOR_VERSION"
        private const val KTOR_DARWIN = "io.ktor:ktor-client-darwin:$KTOR_VERSION"
        private const val KTOR_SERIALIZATION = "io.ktor:ktor-serialization-kotlinx-json:$KTOR_VERSION"
        private const val KTOR_CONTENT_NEGOTIATION = "io.ktor:ktor-client-content-negotiation:$KTOR_VERSION"
        private const val KTOR_LOGGING = "io.ktor:ktor-client-logging:$KTOR_VERSION"
        private const val OKHTTP_LOGGING_INTERCEPTOR =
            "com.squareup.okhttp3:logging-interceptor:$LOGGING_INTERCEPTOR_VERSION"

        private const val KOTLIN_SERIALIZATION =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:$KOTLIN_SERIALIZATION_VERSION"

        val androidDeps = listOf(KTOR_OKHTTP, OKHTTP_LOGGING_INTERCEPTOR)
        val iosDeps = listOf(KTOR_DARWIN)
        val multiplatformDeps = listOf(
            KTOR_CORE, KTOR_SERIALIZATION, KTOR_CONTENT_NEGOTIATION, KTOR_LOGGING,
        )
        val serializationDeps = listOf(
            KOTLIN_SERIALIZATION
        )
    }

    object Serialization

    object UI {
        private const val MATERIAL_VERSION = "1.9.0"
        private const val SPLASH_SCREEN_VERSION = "1.0.0"

        private const val MATERIAL = "com.google.android.material:material:$MATERIAL_VERSION"
        private const val SPLASH_SCREEN = "androidx.core:core-splashscreen:$SPLASH_SCREEN_VERSION"

        val allDeps = listOf(MATERIAL, SPLASH_SCREEN)
    }

    object DataStore {
        private const val VERSION = "1.1.1"

        private const val DATA_STORE = "androidx.datastore:datastore-preferences:$VERSION"

        val allDeps = listOf(DATA_STORE)
    }

    object UsbDrivers {
        private const val VERSION = "3.7.0"

        private const val USB_DRIVERS = "com.github.mik3y:usb-serial-for-android:$VERSION"

        val allDeps = listOf(USB_DRIVERS)
    }

    object Charts {
        private const val VICO_VERSION = "2.0.0-alpha.14"

        private const val VICO_COMPOSE = "com.patrykandpatrick.vico:compose:$VICO_VERSION"
        private const val VICO_MATERIAL = "com.patrykandpatrick.vico:compose-m3:$VICO_VERSION"

        val allDeps = listOf(VICO_COMPOSE, VICO_MATERIAL)
    }

    object GameEngine {
        // ================================================
        // BE VERY CAREFUL WHEN UPDATING KORGE VERSION!!!
        // ================================================
        //
        // After version 3.4.0 authors changed KorGE a lot
        // If you update version, the project will most likely break
        // So be ready to fix it :)
        private const val KORGE_VERSION = "3.4.0"

        private const val KORGE_ANDROID = "com.soywiz.korlibs.korge2:korge-android:$KORGE_VERSION"
        private const val KORGE_GRAPHICS = "com.soywiz.korlibs.korgw:korgw-android:$KORGE_VERSION"
        private const val KORGE_VIDEO = "com.soywiz.korlibs.korvi:korvi-android:$KORGE_VERSION"
        private const val KORGE_IMAGES = "com.soywiz.korlibs.korim:korim-android:$KORGE_VERSION"
        private const val KORGE_AUDIO = "com.soywiz.korlibs.korau:korau-android:$KORGE_VERSION"
        private const val KORGE_DATA_STRUCTURES = "com.soywiz.korlibs.kds:kds-android:$KORGE_VERSION"
        private const val KORGE_MATH = "com.soywiz.korlibs.korma:korma-android:$KORGE_VERSION"

        val allDeps = listOf(
            KORGE_ANDROID, KORGE_GRAPHICS, KORGE_VIDEO, KORGE_IMAGES, KORGE_AUDIO,
            KORGE_DATA_STRUCTURES, KORGE_MATH,
        )
    }

    object Plugins {
        const val DETEKT_VERSION = "1.23.5"

        const val DETEKT_FORMATTING =
            "io.gitlab.arturbosch.detekt:detekt-formatting:$DETEKT_VERSION"
    }

    object Calendar {
        const val CALENDAR1_VERSION = "1.0.1"

        val allDeps = listOf(
            "com.github.playmoweb:MultiDatePicker:$CALENDAR1_VERSION",
        )
    }
}
