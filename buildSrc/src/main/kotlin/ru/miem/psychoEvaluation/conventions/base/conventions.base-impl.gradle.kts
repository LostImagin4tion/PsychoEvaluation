import org.gradle.kotlin.dsl.kotlin
import ru.miem.psychoEvaluation.consts.CompileVersions

/**
 * Basic conventions for api modules which doesn't contains
 * pluggable dependencies
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = CompileVersions.CURRENT_COMPILE_VERSION

    defaultConfig {
        minSdk = CompileVersions.MINIMUM_COMPILE_VERSION
    }
    kotlinOptions {
        jvmTarget = CompileVersions.JVM_TARGET
    }
}
