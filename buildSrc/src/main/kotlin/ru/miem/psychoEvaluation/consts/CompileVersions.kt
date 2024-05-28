package ru.miem.psychoEvaluation.consts

import org.gradle.api.JavaVersion

/**
 * [CompileVersions] is needed for storing actual versions to build the project
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
object CompileVersions {

    const val KOTLIN_VERSION = "1.9.22"
    const val COMPOSE_COMPILE_VERSION = "1.5.9"

    val JAVA_COMPILE_VERSION = JavaVersion.VERSION_1_8
    const val JVM_TARGET = "1.8"

    const val CURRENT_COMPILE_VERSION = 34
    const val MINIMUM_COMPILE_VERSION = 26
}
