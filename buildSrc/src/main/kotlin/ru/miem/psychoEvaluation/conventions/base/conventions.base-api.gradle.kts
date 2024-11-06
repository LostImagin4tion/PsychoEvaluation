import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ru.miem.psychoEvaluation.consts.CompileVersions

/**
 * Basic convention for api modules
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("com.android.library")
    kotlin("android")
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

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = CompileVersions.JVM_TARGET
    }
}