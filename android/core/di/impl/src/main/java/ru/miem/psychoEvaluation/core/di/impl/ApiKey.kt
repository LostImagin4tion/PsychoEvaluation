package ru.miem.psychoEvaluation.core.di.impl

import dagger.MapKey
import ru.miem.psychoEvaluation.core.di.api.DiApi
import kotlin.reflect.KClass

/**
 * [ApiKey] - annotation, which is needed for convenient [DiApi] substitution
 * as key to shared **Map** after *@Provides* methods
 *
 * Use like this:
 * *@ApiKey(SomeStarFeatureApi::class)*
 *
 * @param value - parameter, which provides implementation by [DiApi] key
 *
 * @author Egor Danilov
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ApiKey(val value: KClass<out DiApi>)
