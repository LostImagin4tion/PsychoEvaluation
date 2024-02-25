package ru.miem.psychoEvaluation.core.di.impl.exception

/**
 * Exception for [ru.miem.psychoEvaluation.core.di.impl.ApiResolver]
 *
 * @see ru.miem.psychoEvaluation.core.di.impl.ApiResolver
 *
 * @author Egor Danilov
 */
class ProviderNotFoundException(override val message: String) : RuntimeException()
