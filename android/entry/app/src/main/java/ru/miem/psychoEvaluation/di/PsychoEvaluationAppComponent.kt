package ru.miem.psychoEvaluation.di

import dagger.Component
import ru.miem.psychoEvaluation.core.di.impl.ApiRegistry
import ru.miem.psychoEvaluation.core.di.impl.ApiResolver
import ru.miem.psychoEvaluation.di.modules.FeatureAggregationModule

@Component(
    modules = [
        FeatureAggregationModule::class,
    ]
)
interface PsychoEvaluationAppComponent {

    val apiResolver: ApiResolver
}

/**
 * Initialization of application's **DI graph**
 *
 */
fun initApis(): PsychoEvaluationAppComponent = DaggerPsychoEvaluationAppComponent
    .builder()
    .build()
    .also {
        ApiRegistry.init(it.apiResolver)
    }
