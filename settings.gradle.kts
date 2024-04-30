pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PsychoEvaluation"

// ==== MULTIPLATFORM ====

// Core
include(":multiplatform:core")

// Common
include(":multiplatform:common")

// Feature
include(":multiplatform:feature")

// ==== ANDROID ====

// Entries
include(":android:entry:app")

// Core
include(":android:core:di:api")
include(":android:core:di:impl")

include(":android:core:usbDeviceApi:api")
include(":android:core:usbDeviceApi:impl")

include(":android:core:dataAnalysis:airplaneGame:api")
include(":android:core:dataAnalysis:airplaneGame:impl")

include(":android:core:utils")

// Common
include(":android:common:designSystem")

// Features
include(":android:feature:navigation:api")
include(":android:feature:navigation:impl")

include(":android:feature:authorization:api")
include(":android:feature:authorization:impl")

include(":android:feature:registration:api")
include(":android:feature:registration:impl")

include(":android:feature:userProfile:api")
include(":android:feature:userProfile:impl")

include(":android:feature:trainingsList:api")
include(":android:feature:trainingsList:impl")

include(":android:feature:trainings:debugTraining:api")
include(":android:feature:trainings:debugTraining:impl")

include(":android:feature:trainings:airplaneGame:api")
include(":android:feature:trainings:airplaneGame:impl")
