package ru.miem.psychoEvaluation.core.di.impl

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.core.di.impl.exception.ResolverNotInstalledException

/**
 * [ApiProvider] - Singleton, which wraps **[ApiResolver]**
 *
 * @param apiResolver is needed for storing instance of [ApiResolver] after initialization in *app* module
 *
 * @see ApiKey
 * @see ApiResolver
 *
 * @author Egor Danilov
 */
object ApiRegistry {

    private var apiResolver: ApiResolver? = null

    /**
     * Method for providing [DiApi] from **DI Map** to [ApiResolver].
     *
     * @throws ResolverNotInstalledException in case it is not possible to find the required **Api** by key
     * @return реализация **Api**
     */
    fun <T : DiApi> getApi(cls: Class<T>): T {
        return apiResolver?.getApi(cls)
            ?: throw ResolverNotInstalledException("Resolver is not installed")
    }

    /**
     * Initialization of [apiResolver] property
     *
     * @param newApiResolver - new [ApiResolver] for storing in
     * [ApiRegistry] after initialization in *app* module
     */
    fun init(newApiResolver: ApiResolver) {
        apiResolver = newApiResolver
    }
}
