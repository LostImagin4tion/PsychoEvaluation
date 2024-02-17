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

include(":app")

include(":multiplatform:core")
include(":multiplatform:common")
include(":multiplatform:feature")

include(":android:core")
include(":android:common")
include(":android:feature")
 