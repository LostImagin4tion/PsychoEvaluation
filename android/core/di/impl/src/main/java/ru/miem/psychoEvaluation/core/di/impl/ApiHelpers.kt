package ru.miem.psychoEvaluation.core.di.impl

import ru.miem.psychoEvaluation.core.di.api.DiApi

/**
 * Delegate for lazy injection of [DiApi] properties to class properties
 *
 * Use like this:
 * val logger by api(OdooApi::logger)
 */
inline fun <T, reified A : DiApi> diApi(crossinline getter: (A) -> T): Lazy<T> = lazy { getter(getDiApi()) }

/**
 * Delegate for lazy injection of [DiApi] properties to class properties
 *
 * Use like this:
 * val logger = apiBlocking(OdooApi::logger)
 */
inline fun <T, reified A : DiApi> diApiBlocking(crossinline getter: (A) -> T): T = getter(getDiApi())

/**
 * Method for getting Api (kotlin)
 *
 * Use like this:
 * val someOdooFeatureApi: SomeOdooFeatureApi = getApi()
 */
inline fun <reified T : DiApi> getDiApi(): T = getDiApi(T::class.java)

/**
 * Method fo getting Api (java)
 *
 * Use like this:
 * SomeOdooFeatureApi someOdooFeatureApi = ApiHelpers.getApi(SomeOdooFeatureApi.class)
 */
fun <T : DiApi> getDiApi(cls: Class<T>): T = ApiRegistry.getApi(cls)
