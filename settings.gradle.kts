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


include(":multiplatform:core")
include(":multiplatform:common")
include(":multiplatform:feature")

include(":android:entry:app")

include(":android:core")
include(":android:common")

include(":android:feature:navigation:impl")
