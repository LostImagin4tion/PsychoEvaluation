package ru.miem.psychoEvaluation.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.miem.psychoEvaluation.core.di.impl.ApiRegistry
import ru.miem.psychoEvaluation.core.di.impl.ApiResolver
import ru.miem.psychoEvaluation.di.modules.CommonAggregationModule
import ru.miem.psychoEvaluation.di.modules.CoreAggregationModule
import ru.miem.psychoEvaluation.di.modules.FeatureAggregationModule

@Component(
    modules = [
        CoreAggregationModule::class,
        CommonAggregationModule::class,
        FeatureAggregationModule::class,
    ]
)
interface PsychoEvaluationAppComponent {

    val apiResolver: ApiResolver

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun build(): PsychoEvaluationAppComponent
    }
}

/**
 * Initialization of application's **DI graph**
 */
fun initApis(context: Context): PsychoEvaluationAppComponent = DaggerPsychoEvaluationAppComponent
    .builder()
    .context(context)
    .build()
    .also {
        ApiRegistry.init(it.apiResolver)
    }
