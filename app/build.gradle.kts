plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.miem.psychoEvaluation"
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
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = CompileVersions.JAVA_COMPILE_VERSION
        targetCompatibility = CompileVersions.JAVA_COMPILE_VERSION
    }
    kotlinOptions {
        jvmTarget = CompileVersions.JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = CompileVersions.COMPOSE_COMPILE_VERSION
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    implementation("androidx.compose.ui:ui-graphics")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
}