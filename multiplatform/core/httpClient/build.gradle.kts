import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import ru.miem.psychoEvaluation.consts.CompileVersions
import ru.miem.psychoEvaluation.consts.Dependencies

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    // https://kotlinlang.org/docs/multiplatform-expect-actual.html#expected-and-actual-classes
    // To suppress this warning about usage of expected and actual classes
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = CompileVersions.JVM_TARGET
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "httpClient"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            Dependencies.Network.multiplatformDeps.forEach { implementation(it) }

            implementation(project(":multiplatform:core:utils"))
        }
        androidMain.dependencies {
            Dependencies.Network.androidDeps.forEach { implementation(it) }

            Dependencies.Dagger.implDeps.forEach { implementation(it) }
//            Dependencies.Dagger.kaptDeps.forEach { kapt(it) }

            api(project(":android:core:di:api"))
        }
        iosMain.dependencies {
            Dependencies.Network.iosDeps.forEach { implementation(it) }
        }
        commonTest.dependencies {
        }
    }
}

android {
    namespace = "ru.miem.psychoEvaluation.multiplatform.core.httpClient"
    compileSdk = CompileVersions.CURRENT_COMPILE_VERSION

    defaultConfig {
        minSdk = CompileVersions.MINIMUM_COMPILE_VERSION
    }

    compileOptions {
        sourceCompatibility = CompileVersions.JAVA_COMPILE_VERSION
        targetCompatibility = CompileVersions.JAVA_COMPILE_VERSION
    }
}
