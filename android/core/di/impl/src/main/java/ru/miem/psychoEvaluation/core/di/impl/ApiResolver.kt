package ru.miem.psychoEvaluation.core.di.impl

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.core.di.impl.exception.ProviderNotFoundException
import javax.inject.Inject

/**
 * [ApiResolver] - class, that encapsulates **Map** with all implementations of **Api**
 *
 * @constructor contains [diApiFactories], where the key is [DiApi] and the value is [ApiProvider] with
 * implementation
 *
 * @see ApiProvider
 *
 * @author Egor Danilov
 */
class ApiResolver @Inject constructor(
    private val diApiFactories: Map<Class<out DiApi>, @JvmSuppressWildcards ApiProvider>
) {

    /**
     * Takes [ApiProvider] from [diApiFactories]
     *
     * @param cls - key, by which we get [ApiProvider] from [diApiFactories]
     * @return [ApiProvider] corresponding to key [cls]
     */
    private fun <T : DiApi> getProvider(cls: Class<T>): ApiProvider {
        return diApiFactories[cls]
            ?: throw ProviderNotFoundException("Not found provider for class $cls")
    }

    /**
     * Calls method [getProvider] for providing required [ApiProvider] and
     * casting to required Generic
     *
     * @param cls - key, by which we get [ApiProvider] from [diApiFactories]
     * @return [T] - required implementation of [DiApi]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : DiApi> getApi(cls: Class<T>): T = getProvider(cls).get() as T
}
