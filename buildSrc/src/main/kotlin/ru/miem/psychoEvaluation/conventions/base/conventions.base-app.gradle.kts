import org.gradle.kotlin.dsl.kotlin
import ru.miem.psychoEvaluation.consts.CompileVersions

/**
 * Basic convention for app modules
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = CompileVersions.CURRENT_COMPILE_VERSION

    defaultConfig {
        applicationId = "ru.miem.psychoEvaluation"
        minSdk = CompileVersions.MINIMUM_COMPILE_VERSION
        targetSdk = CompileVersions.CURRENT_COMPILE_VERSION
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = CompileVersions.JVM_TARGET
    }
    compileOptions {
        sourceCompatibility = CompileVersions.JAVA_COMPILE_VERSION
        targetCompatibility = CompileVersions.JAVA_COMPILE_VERSION
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
