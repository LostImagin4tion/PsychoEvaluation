pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
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
include(":android:core")

// Common
include(":android:common:designSystem")

// Features
include(":android:feature:navigation:api")
include(":android:feature:navigation:impl")
