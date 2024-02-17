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
    id("org.jetbrains.kotlin.android")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = CompileVersions.JVM_TARGET
    }
}