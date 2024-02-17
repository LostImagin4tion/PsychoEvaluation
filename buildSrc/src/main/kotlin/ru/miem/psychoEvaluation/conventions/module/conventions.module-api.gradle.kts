import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Convention for api modules, which contains [conventions.base-api] and
 * pluggs in necessary dependencies
 *
 * @author Egor Danilov
 * @since 17.02.2024
 */
plugins {
    id("conventions.base-api")
}

dependencies {
}