// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("jvm") apply false
    kotlin("android") apply false
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") version ru.miem.psychoEvaluation.consts.CompileVersions.KOTLIN_VERSION apply false
    id("io.gitlab.arturbosch.detekt") version ru.miem.psychoEvaluation.consts.Dependencies.Plugins.DETEKT_VERSION
}

tasks {
    val detektAll by registering(io.gitlab.arturbosch.detekt.Detekt::class) {
        parallel = true
        setSource(files(projectDir))
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
        exclude("**/**est**")
        config.setFrom(files("${rootDir}/.detekt/detekt-config.yml"))
        buildUponDefaultConfig = false
        ignoreFailures = false
        autoCorrect = true
        dependencies {
            detektPlugins(ru.miem.psychoEvaluation.consts.Dependencies.Plugins.DETEKT_FORMATTING)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}