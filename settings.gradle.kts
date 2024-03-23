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
include(":android:core:di:api")
include(":android:core:di:impl")

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

include(":android:feature:trainings:api")
include(":android:feature:trainings:impl")
