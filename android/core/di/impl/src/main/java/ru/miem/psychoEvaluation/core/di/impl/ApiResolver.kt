package ru.miem.psychoEvaluation.core.di.impl

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.core.di.impl.exception.ProviderNotFoundException
import javax.inject.Inject

/**
 * [ApiResolver] - class, that encapsulates **Map** with all implementations of **Api**
 *
 * @constructor contains [apiFactories], where the key is [Api] and the value is [ApiProvider] with
 * implementation
 *
 * @see ApiProvider
 *
 * @author Egor Danilov
 */
class ApiResolver @Inject constructor(
    private val apiFactories: Map<Class<out Api>, @JvmSuppressWildcards ApiProvider>
) {

    /**
     * Takes [ApiProvider] from [apiFactories]
     *
     * @param cls - key, by which we get [ApiProvider] from [apiFactories]
     * @return [ApiProvider] corresponding to key [cls]
     */
    private fun <T : Api> getProvider(cls: Class<T>): ApiProvider {
        return apiFactories[cls]
            ?: throw ProviderNotFoundException("Not found provider for class $cls")
    }

    /**
     * Calls method [getProvider] for providing required [ApiProvider] and
     * casting to required Generic
     *
     * @param cls - key, by which we get [ApiProvider] from [apiFactories]
     * @return [T] - required implementation of [Api]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Api> getApi(cls: Class<T>): T = getProvider(cls).get() as T
}
