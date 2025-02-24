package ru.miem.psychoEvaluation.core.di.impl

import ru.miem.psychoEvaluation.core.di.api.DiApi

/**
 * [ApiProvider] - SAM interface for wrapping implementation of [DiApi] and subsequent addition
 * to general **DI Map** of [ApiResolver]
 *
 * Use like this:
 * ApiProvider { SomeFeatureApiProvider.create() }
 * or ApiProvider(SomeFeatureApiProvider::create)
 *
 * @see ApiResolver
 *
 * @author Egor Danilov
 */
fun interface ApiProvider {
    fun get(): DiApi
}
